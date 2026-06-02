package com.observability.sfdc.service

import io.minio.*
import io.minio.http.Method
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

@Service
class MinioService(
    private val minioClient: MinioClient,
    @Value($$"${minio.bucket-name}") private val bucketName: String
) {
    private val logger = LoggerFactory.getLogger(MinioService::class.java)

    init {
        ensureBucketExists()
    }

    private fun ensureBucketExists() {
        try {
            val found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
                logger.info("Created MinIO bucket: $bucketName")
            }
        } catch (e: Exception) {
            logger.error("Error ensuring MinIO bucket exists: ${e.message}")
        }
    }

    @Async
    fun uploadLog(logId: String, body: String) {
        try {
            val compressedData = compress(body)
            val inputStream = ByteArrayInputStream(compressedData)
            
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`("$logId.log.gz")
                    .stream(inputStream, compressedData.size.toLong(), -1)
                    .contentType("application/gzip")
                    .build()
            )
            logger.info("Uploaded compressed log $logId to MinIO")
        } catch (e: Exception) {
            logger.error("Failed to upload log $logId to MinIO: ${e.message}")
        }
    }

    fun downloadLog(logId: String): String? {
        return try {
            val stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .`object`("$logId.log.gz")
                    .build()
            )
            val compressedData = stream.use { it.readAllBytes() }
            decompress(compressedData)
        } catch (e: Exception) {
            // Log not found or other error - return null to fallback
            logger.error("Error downloading log $logId to MinIO: ${e.message}")
            null
        }
    }

    /**
     * Generates a pre-signed URL for direct download from MinIO.
     * The URL is valid for 15 minutes.
     */
    fun generateDownloadUrl(logId: String, downloadName: String): String? {
        return try {
            minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .`object`("$logId.log.gz")
                    .expiry(15, TimeUnit.MINUTES)
                    .extraQueryParams(mapOf(
                        "response-content-disposition" to "attachment; filename=\"$downloadName.log.gz\"",
                        "response-content-type" to "application/gzip"
                    ))
                    .build()
            )
        } catch (e: Exception) {
            logger.error("Failed to generate pre-signed URL for $logId: ${e.message}")
            null
        }
    }

    private fun compress(data: String): ByteArray {
        val bos = ByteArrayOutputStream()
        GZIPOutputStream(bos).use { it.write(data.toByteArray()) }
        return bos.toByteArray()
    }

    private fun decompress(compressedData: ByteArray): String {
        val bis = ByteArrayInputStream(compressedData)
        return GZIPInputStream(bis).bufferedReader().use { it.readText() }
    }
}
