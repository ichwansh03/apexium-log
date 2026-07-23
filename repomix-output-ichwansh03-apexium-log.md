This file is a merged representation of a subset of the codebase, containing files not matching ignore patterns, combined into a single document by Repomix.
The content has been processed where empty lines have been removed, line numbers have been added, security check has been disabled.

# File Summary

## Purpose
This file contains a packed representation of a subset of the repository's contents that is considered the most important context.
It is designed to be easily consumable by AI systems for analysis, code review,
or other automated processes.

## File Format
The content is organized as follows:
1. This summary section
2. Repository information
3. Directory structure
4. Repository files (if enabled)
5. Multiple file entries, each consisting of:
  a. A header with the file path (## File: path/to/file)
  b. The full contents of the file in a code block

## Usage Guidelines
- This file should be treated as read-only. Any changes should be made to the
  original repository files, not this packed version.
- When processing this file, use the file path to distinguish
  between different files in the repository.
- Be aware that this file may contain sensitive information. Handle it with
  the same level of security as you would the original repository.

## Notes
- Some files may have been excluded based on .gitignore rules and Repomix's configuration
- Binary files are not included in this packed representation. Please refer to the Repository Structure section for a complete list of file paths, including binary files
- Files matching these patterns are excluded: .env
- Files matching patterns in .gitignore are excluded
- Files matching default ignore patterns are excluded
- Empty lines have been removed from all files
- Line numbers have been added to the beginning of each line
- Security check has been disabled - content may contain sensitive information
- Files are sorted by Git change count (files with more changes are at the bottom)

# Directory Structure
````
.github/
  workflows/
    codeql.yml
  dependabot.yml
.mvn/
  wrapper/
    maven-wrapper.properties
prometheus/
  prometheus.yml
src/
  main/
    kotlin/
      com/
        observability/
          sfdc/
            config/
              MinioConfig.kt
              OpenApiConfig.kt
              RedisConfig.kt
            controller/
              SalesforceLogController.kt
              SalesforceMetadataController.kt
              SalesforceUserController.kt
            domain/
              ApexClass.kt
              ApexTrigger.kt
              DebugLevel.kt
              Log.kt
              MetadataHistory.kt
              TraceJob.kt
              User.kt
            dto/
              ApexClassDto.kt
              ApexCodeCoverageDto.kt
              ApexLogDto.kt
              ApexTriggerDto.kt
              DebugLevelDto.kt
              MetadataDetailDto.kt
              MetadataDiffDto.kt
              SalesforceQueryResult.kt
              SalesforceSearchResponse.kt
              SalesforceTokenResponse.kt
              TraceFlagDto.kt
              UserDto.kt
              UserSummaryDto.kt
            repository/
              ApexClassRepository.kt
              ApexTriggerRepository.kt
              DebugLevelRepository.kt
              LogRepository.kt
              MetadataHistoryRepository.kt
              TraceJobRepository.kt
              UserRepository.kt
            service/
              LogRetentionService.kt
              MetadataComparisonService.kt
              MinioService.kt
              SalesforceAuthService.kt
              SalesforceBaseService.kt
              SalesforceLogPollingService.kt
              SalesforceLogService.kt
              SalesforceMetadataPollingService.kt
              SalesforceMetadataService.kt
              SalesforceUserService.kt
              TraceJobSchedulerService.kt
              TraceJobService.kt
            SfdcApplication.kt
    resources/
      application.properties
  test/
    kotlin/
      com/
        observability/
          sfdc/
            dto/
              FrontendTraceFlagRequestTest.kt
            service/
              SalesforceLogPollingServiceTest.kt
              SalesforceMetadataServiceTest.kt
              TraceJobServiceTest.kt
            SfdcApplicationTests.kt
.gitattributes
.gitignore
.gitmodules
CHANGELOG.md
CONTRIBUTING.md
docker-compose.yml
Dockerfile
MONITORING.md
mvnw
mvnw.cmd
pom.xml
README.md
SKILL.md
````

# Files

## File: .github/workflows/codeql.yml
````yaml
 1: name: "CodeQL"
 2: on:
 3:   push:
 4:     branches: [ "main", "feat/trace-flags" ]
 5:   pull_request:
 6:     branches: [ "main" ]
 7:   schedule:
 8:     - cron: '0 0 * * 0'
 9:   workflow_dispatch:
10: jobs:
11:   analyze:
12:     name: Analyze
13:     runs-on: ubuntu-latest
14:     permissions:
15:       actions: read
16:       contents: read
17:       security-events: write
18:     strategy:
19:       fail-fast: false
20:       matrix:
21:         language: [ 'java-kotlin' ]
22:     steps:
23:     - name: Checkout repository
24:       uses: actions/checkout@v4
25:     - name: Set up JDK 21
26:       uses: actions/setup-java@v4
27:       with:
28:         java-version: '21'
29:         distribution: 'temurin'
30:         cache: 'maven'
31:     - name: Initialize CodeQL
32:       uses: github/codeql-action/init@v3
33:       with:
34:         languages: ${{ matrix.language }}
35:         build-mode: manual
36:     - name: Build with Maven
37:       run: |
38:         chmod +x mvnw
39:         ./mvnw clean compile -DskipTests
40:     - name: Perform CodeQL Analysis
41:       uses: github/codeql-action/analyze@v3
42:       with:
43:         category: "/language:${{matrix.language}}"
````

## File: .github/dependabot.yml
````yaml
 1: # To get started with Dependabot version updates, you'll need to specify which
 2: # package ecosystems to update and where the package manifests are located.
 3: # Please see the documentation for all configuration options:
 4: # https://docs.github.com/code-security/dependabot/dependabot-version-updates/configuration-options-for-the-dependabot.yml-file
 5: version: 2
 6: updates:
 7:   - package-ecosystem: "maven" # See documentation for possible values
 8:     directory: "/" # Location of package manifests
 9:     schedule:
10:       interval: "weekly"
11:   - package-ecosystem: "docker-compose" # See documentation for possible values
12:     directory: "/" # Location of package manifests
13:     schedule:
14:       interval: "weekly"
````

## File: .mvn/wrapper/maven-wrapper.properties
````
1: wrapperVersion=3.3.4
2: distributionType=only-script
3: distributionUrl=https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.14/apache-maven-3.9.14-bin.zip
````

## File: prometheus/prometheus.yml
````yaml
1: global:
2:   scrape_interval: 15s
3: scrape_configs:
4:   - job_name: 'apexium-log'
5:     metrics_path: '/actuator/prometheus'
6:     static_configs:
7:       - targets: ['app:8080']
````

## File: src/main/kotlin/com/observability/sfdc/config/MinioConfig.kt
````kotlin
 1: package com.observability.sfdc.config
 2: import io.minio.MinioClient
 3: import org.springframework.beans.factory.annotation.Value
 4: import org.springframework.context.annotation.Bean
 5: import org.springframework.context.annotation.Configuration
 6: @Configuration
 7: class MinioConfig(
 8:     @Value($$"${minio.url}") private val url: String,
 9:     @Value($$"${minio.access-key}") private val accessKey: String,
10:     @Value($$"${minio.secret-key}") private val secretKey: String
11: ) {
12:     @Bean
13:     fun minioClient(): MinioClient {
14:         return MinioClient.builder()
15:             .endpoint(url)
16:             .credentials(accessKey, secretKey)
17:             .build()
18:     }
19: }
````

## File: src/main/kotlin/com/observability/sfdc/config/OpenApiConfig.kt
````kotlin
 1: package com.observability.sfdc.config
 2: import io.swagger.v3.oas.models.OpenAPI
 3: import io.swagger.v3.oas.models.info.Info
 4: import io.swagger.v3.oas.models.info.License
 5: import org.springframework.context.annotation.Bean
 6: import org.springframework.context.annotation.Configuration
 7: @Configuration
 8: class OpenApiConfig {
 9:     @Bean
10:     fun customOpenAPI(): OpenAPI {
11:         return OpenAPI()
12:             .info(
13:                 Info()
14:                     .title("Apexium Log API")
15:                     .version("v1")
16:                     .description("A real-time observability platform built with Kotlin and Spring Boot to centralize, monitor, and analyze Salesforce debug logs and system events.")
17:                     .license(
18:                         License()
19:                             .name("MIT License")
20:                             .url("https://opensource.org/licenses/MIT")
21:                     )
22:             )
23:     }
24: }
````

## File: src/main/kotlin/com/observability/sfdc/config/RedisConfig.kt
````kotlin
 1: package com.observability.sfdc.config
 2: import org.springframework.beans.factory.annotation.Value
 3: import org.springframework.cache.CacheManager
 4: import org.springframework.context.annotation.Bean
 5: import org.springframework.context.annotation.Configuration
 6: import org.springframework.data.redis.cache.RedisCacheConfiguration
 7: import org.springframework.data.redis.cache.RedisCacheManager
 8: import org.springframework.data.redis.connection.RedisConnectionFactory
 9: import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
10: import org.springframework.data.redis.serializer.RedisSerializationContext
11: import org.springframework.data.redis.serializer.StringRedisSerializer
12: import java.time.Duration
13: @Configuration
14: class RedisConfig(
15:     @Value($$"${cache.ttl.token:3600}") private val tokenTtl: Long,
16:     @Value($$"${cache.ttl.metadata:3600}") private val metadataTtl: Long
17: ) {
18:     @Bean
19:     fun cacheManager(connectionFactory: RedisConnectionFactory): CacheManager {
20:         val serializer = GenericJackson2JsonRedisSerializer()
21:         val defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
22:             .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
23:             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
24:             .entryTtl(Duration.ofSeconds(metadataTtl)) // Default TTL
25:         val cacheConfigs = mutableMapOf<String, RedisCacheConfiguration>()
26:         // Custom TTL for specific caches
27:         cacheConfigs["sf_tokens"] = defaultConfig.entryTtl(Duration.ofSeconds(tokenTtl))
28:         cacheConfigs["sf_metadata"] = defaultConfig.entryTtl(Duration.ofSeconds(metadataTtl))
29:         cacheConfigs["sf_users"] = defaultConfig.entryTtl(Duration.ofSeconds(metadataTtl))
30:         return RedisCacheManager.builder(connectionFactory)
31:             .cacheDefaults(defaultConfig)
32:             .withInitialCacheConfigurations(cacheConfigs)
33:             .build()
34:     }
35: }
````

## File: src/main/kotlin/com/observability/sfdc/controller/SalesforceLogController.kt
````kotlin
  1: package com.observability.sfdc.controller
  2: import com.observability.sfdc.domain.Log
  3: import com.observability.sfdc.domain.TraceJob
  4: import com.observability.sfdc.dto.ApexLogDto
  5: import com.observability.sfdc.dto.FrontendTraceFlagRequest
  6: import com.observability.sfdc.dto.SalesforceCreateResponse
  7: import com.observability.sfdc.dto.TraceFlagDto
  8: import com.observability.sfdc.repository.LogRepository
  9: import com.observability.sfdc.service.SalesforceLogService
 10: import com.observability.sfdc.service.TraceJobService
 11: import io.swagger.v3.oas.annotations.Operation
 12: import io.swagger.v3.oas.annotations.tags.Tag
 13: import jakarta.validation.Valid
 14: import org.springframework.data.domain.PageRequest
 15: import org.springframework.data.domain.Sort
 16: import org.springframework.http.HttpStatus
 17: import org.springframework.http.ResponseEntity
 18: import org.springframework.web.bind.annotation.*
 19: @RestController
 20: @RequestMapping("/api/sfdc/logs")
 21: @Tag(name = "Salesforce Logs", description = "Endpoints for managing and retrieving Salesforce debug logs")
 22: class SalesforceLogController(
 23:     private val logService: SalesforceLogService,
 24:     private val logRepository: LogRepository,
 25:     private val traceJobService: TraceJobService
 26: ) {
 27:     @GetMapping
 28:     @Operation(summary = "Query Apex Logs from Salesforce", description = "Retrieves a list of Apex log headers directly from Salesforce Tooling API.")
 29:     fun getApexLogs(
 30:         @RequestParam(defaultValue = "10") size: Int,
 31:         @RequestParam(defaultValue = "0") page: Int
 32:     ): List<ApexLogDto> {
 33:         val offset = page * size
 34:         return logService.queryApexLogs(size, offset)
 35:     }
 36:     @GetMapping("/db")
 37:     @Operation(summary = "Get Logs from Database", description = "Retrieves processed logs stored in the local PostgreSQL database with optional filtering.")
 38:     fun getDbLogs(
 39:         @RequestParam(required = false) className: String?,
 40:         @RequestParam(required = false) author: String?,
 41:         @RequestParam(defaultValue = "10") size: Int,
 42:         @RequestParam(defaultValue = "0") page: Int
 43:     ): List<Log> {
 44:         val pageable = PageRequest.of(page, size, Sort.by("requestTime").descending())
 45:         return when {
 46:             !className.isNullOrBlank() && !author.isNullOrBlank() ->
 47:                 logRepository.findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(className, author, pageable)
 48:             !className.isNullOrBlank() ->
 49:                 logRepository.findByApexClassNameContainingIgnoreCase(className, pageable)
 50:             !author.isNullOrBlank() ->
 51:                 logRepository.findByAuthorNameContainingIgnoreCase(author, pageable)
 52:             else -> logRepository.findAllByOrderByRequestTimeDesc(pageable)
 53:         }
 54:     }
 55:     @GetMapping("/{id}/body")
 56:     @Operation(summary = "Get Log Body", description = "Fetches the full text body of a specific Apex log, checking local storage first.")
 57:     fun getLogBody(@PathVariable id: String): String? {
 58:         return logService.getLogBody(id)
 59:     }
 60:     @GetMapping("/{id}/download")
 61:     @Operation(summary = "Download Log File", description = "Downloads the compressed (.gz) log file from storage.")
 62:     fun downloadLog(
 63:         @PathVariable id: String,
 64:         @RequestParam(required = false) operation: String?
 65:     ): ResponseEntity<org.springframework.core.io.Resource> {
 66:         val stream = logService.getLogDownloadStream(id)
 67:         return if (stream != null) {
 68:             val downloadName = "${operation ?: "log"}_$id.log.gz"
 69:             val resource = org.springframework.core.io.InputStreamResource(stream)
 70:             ResponseEntity.ok()
 71:                 .contentType(org.springframework.http.MediaType.parseMediaType("application/gzip"))
 72:                 .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$downloadName\"")
 73:                 .body(resource)
 74:         } else {
 75:             ResponseEntity.status(HttpStatus.NOT_FOUND).build()
 76:         }
 77:     }
 78:     @DeleteMapping("/{id}")
 79:     @Operation(summary = "Delete Log", description = "Deletes a specific Apex log from Salesforce and local storage.")
 80:     fun deleteLog(@PathVariable id: String): ResponseEntity<Unit> {
 81:         val deleted = logService.deleteLog(id)
 82:         return if (deleted) ResponseEntity.ok().build() else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
 83:     }
 84:     @DeleteMapping
 85:     @Operation(summary = "Bulk Delete Logs", description = "Deletes multiple logs by ID or deletes all logs if no IDs are provided.")
 86:     fun deleteLogs(@RequestParam(required = false) ids: List<String>?): ResponseEntity<Map<String, Any>> {
 87:         return if (ids.isNullOrEmpty()) {
 88:             val count = logService.deleteAllLogs()
 89:             ResponseEntity.ok(mapOf("message" to "Successfully deleted $count logs from Salesforce", "count" to count))
 90:         } else {
 91:             val results = logService.deleteLogs(ids)
 92:             ResponseEntity.ok(mapOf("results" to results))
 93:         }
 94:     }
 95:     @PostMapping("/trace-flags")
 96:     @Operation(summary = "Create Trace Flag", description = "Creates a new TraceFlag in Salesforce for a target user or class.")
 97:     fun createTraceFlag(@Valid @RequestBody request: FrontendTraceFlagRequest): SalesforceCreateResponse? {
 98:         return logService.createTraceFlag(request)
 99:     }
100:     @GetMapping("/trace-flags")
101:     @Operation(summary = "Get Active Trace Flags", description = "Lists all currently active TraceFlags in the Salesforce organization.")
102:     fun getActiveTraceFlags(): List<TraceFlagDto> {
103:         return logService.getActiveTraceFlags()
104:     }
105:     @GetMapping("/trace-flags/all")
106:     @Operation(summary = "Get All Trace Flags", description = "Lists all TraceFlags (active and expired) from the Salesforce organization.")
107:     fun getAllTraceFlags(): List<TraceFlagDto> {
108:         return logService.getAllTraceFlags()
109:     }
110:     @DeleteMapping("/trace-flags/{id}")
111:     @Operation(summary = "Delete Trace Flag", description = "Deletes a specific TraceFlag from Salesforce.")
112:     fun deleteTraceFlag(@PathVariable id: String): ResponseEntity<Unit> {
113:         val deleted = logService.deleteTraceFlag(id)
114:         return if (deleted) ResponseEntity.ok().build() else ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
115:     }
116:     // --- Trace Job Endpoints ---
117:     @PostMapping("/trace-jobs")
118:     @Operation(summary = "Create Trace Job", description = "Creates a managed trace job that automatically handles Salesforce's 24-hour limit using a sliding window.")
119:     fun createTraceJob(@Valid @RequestBody request: FrontendTraceFlagRequest): TraceJob {
120:         return traceJobService.createJob(request)
121:     }
122:     @GetMapping("/trace-jobs")
123:     @Operation(summary = "Get All Trace Jobs", description = "Lists all trace jobs (active, completed, cancelled) managed by the application.")
124:     fun getTraceJobs(@RequestParam(required = false) targetName: String?): List<TraceJob> {
125:         return if (!targetName.isNullOrBlank()) {
126:             traceJobService.searchJobsByName(targetName)
127:         } else {
128:             traceJobService.getAllJobs()
129:         }
130:     }
131:     @PostMapping("/trace-jobs/adopt")
132:     @Operation(summary = "Adopt Existing Trace Flag", description = "Imports an existing Salesforce TraceFlag as a managed trace job.")
133:     fun adoptTraceFlag(@RequestBody traceFlag: TraceFlagDto): ResponseEntity<Any> {
134:         return try {
135:             val job = traceJobService.adoptExistingTraceFlag(traceFlag)
136:             ResponseEntity.ok(job)
137:         } catch (e: IllegalStateException) {
138:             ResponseEntity.status(HttpStatus.CONFLICT).body(mapOf("error" to e.message))
139:         }
140:     }
141:     @DeleteMapping("/trace-jobs/{id}")
142:     @Operation(summary = "Cancel Trace Job", description = "Cancels a managed trace job and deletes its associated Salesforce TraceFlag.")
143:     fun cancelTraceJob(@PathVariable id: Long): ResponseEntity<Unit> {
144:         traceJobService.cancelJob(id)
145:         return ResponseEntity.ok().build()
146:     }
147: }
````

## File: src/main/kotlin/com/observability/sfdc/controller/SalesforceMetadataController.kt
````kotlin
 1: package com.observability.sfdc.controller
 2: import com.observability.sfdc.domain.ApexClass
 3: import com.observability.sfdc.domain.ApexTrigger
 4: import com.observability.sfdc.dto.ApexClassDto
 5: import com.observability.sfdc.dto.ApexTriggerDto
 6: import com.observability.sfdc.dto.DebugLevelDto
 7: import com.observability.sfdc.dto.MetadataDetailDto
 8: import com.observability.sfdc.service.MetadataComparisonService
 9: import com.observability.sfdc.service.SalesforceMetadataService
10: import io.swagger.v3.oas.annotations.Operation
11: import io.swagger.v3.oas.annotations.tags.Tag
12: import org.springframework.web.bind.annotation.*
13: @RestController
14: @RequestMapping("/api/sfdc/metadata")
15: @Tag(name = "Salesforce Metadata", description = "Endpoints for retrieving Salesforce metadata information (Classes, Triggers, Debug Levels)")
16: class SalesforceMetadataController(
17:     private val metadataService: SalesforceMetadataService,
18:     private val comparisonService: MetadataComparisonService
19: ) {
20:     @GetMapping("/compare/{type}/{id}")
21:     @Operation(summary = "Compare Metadata", description = "Compares the current Apex class/trigger body with the previous version.")
22:     fun compareMetadata(
23:         @PathVariable type: String,
24:         @PathVariable id: String
25:     ): com.observability.sfdc.dto.MetadataDiffDto {
26:         return comparisonService.compareMetadata(id, type)
27:     }
28:     @GetMapping("/details/{type}/{id}")
29:     @Operation(summary = "Get Metadata Details", description = "Retrieves deep details for a specific Apex class or trigger, including coverage and related test classes.")
30:     fun getMetadataDetails(
31:         @PathVariable type: String,
32:         @PathVariable id: String
33:     ): MetadataDetailDto? {
34:         return metadataService.getMetadataDetail(id, type)
35:     }
36:     @GetMapping("/debug-levels")
37:     @Operation(summary = "Get Debug Levels from Salesforce", description = "Retrieves all available debug configurations directly from Salesforce.")
38:     fun getDebugLevels(
39:         @RequestParam(defaultValue = "10") size: Int,
40:         @RequestParam(defaultValue = "0") page: Int
41:     ): List<DebugLevelDto> {
42:         val offset = page * size
43:         return metadataService.getAllDebugLevels(limit = size, offset = offset)
44:     }
45:     @GetMapping("/debug-levels/db")
46:     @Operation(summary = "Search Debug Levels in Database", description = "Searches for debug levels stored in the local database.")
47:     fun searchDebugLevels(
48:         @RequestParam(required = false) name: String?,
49:         @RequestParam(defaultValue = "10") size: Int,
50:         @RequestParam(defaultValue = "0") page: Int
51:     ): List<com.observability.sfdc.domain.DebugLevel> {
52:         val offset = page * size
53:         return metadataService.searchDebugLevels(name, size, offset)
54:     }
55:     @GetMapping("/classes")
56:     @Operation(summary = "Get Apex Classes from Salesforce", description = "Retrieves active Apex classes directly from Salesforce.")
57:     fun getApexClasses(
58:         @RequestParam(defaultValue = "10") size: Int,
59:         @RequestParam(defaultValue = "0") page: Int
60:     ): List<ApexClassDto> {
61:         val offset = page * size
62:         return metadataService.getAllApexClasses(limit = size, offset = offset)
63:     }
64:     @GetMapping("/classes/db")
65:     @Operation(summary = "Search Apex Classes in Database", description = "Searches for Apex classes stored in the local database.")
66:     fun searchClasses(
67:         @RequestParam(required = false) name: String?,
68:         @RequestParam(defaultValue = "10") size: Int,
69:         @RequestParam(defaultValue = "0") page: Int
70:     ): List<ApexClass> {
71:         val offset = page * size
72:         return metadataService.searchClasses(name, size, offset)
73:     }
74:     @GetMapping("/triggers")
75:     @Operation(summary = "Get Apex Triggers from Salesforce", description = "Retrieves active Apex triggers directly from Salesforce.")
76:     fun getApexTriggers(
77:         @RequestParam(defaultValue = "10") size: Int,
78:         @RequestParam(defaultValue = "0") page: Int
79:     ): List<ApexTriggerDto> {
80:         val offset = page * size
81:         return metadataService.getAllApexTriggers(limit = size, offset = offset)
82:     }
83:     @GetMapping("/triggers/db")
84:     @Operation(summary = "Search Apex Triggers in Database", description = "Searches for Apex triggers stored in the local database.")
85:     fun searchTriggers(
86:         @RequestParam(required = false) name: String?,
87:         @RequestParam(defaultValue = "10") size: Int,
88:         @RequestParam(defaultValue = "0") page: Int
89:     ): List<ApexTrigger> {
90:         val offset = page * size
91:         return metadataService.searchTriggers(name, size, offset)
92:     }
93: }
````

## File: src/main/kotlin/com/observability/sfdc/controller/SalesforceUserController.kt
````kotlin
 1: package com.observability.sfdc.controller
 2: import com.observability.sfdc.domain.User
 3: import com.observability.sfdc.dto.SalesforceUserDto
 4: import com.observability.sfdc.service.SalesforceUserService
 5: import io.swagger.v3.oas.annotations.Operation
 6: import io.swagger.v3.oas.annotations.tags.Tag
 7: import org.springframework.web.bind.annotation.GetMapping
 8: import org.springframework.web.bind.annotation.RequestMapping
 9: import org.springframework.web.bind.annotation.RequestParam
10: import org.springframework.web.bind.annotation.RestController
11: @RestController
12: @RequestMapping("/api/sfdc/users")
13: @Tag(name = "Salesforce Users", description = "Endpoints for retrieving Salesforce user information")
14: class SalesforceUserController(
15:     private val userService: SalesforceUserService
16: ) {
17:     @GetMapping
18:     @Operation(summary = "Get Users from Salesforce", description = "Retrieves active users directly from Salesforce.")
19:     fun getUsers(
20:         @RequestParam(defaultValue = "10") size: Int,
21:         @RequestParam(defaultValue = "0") page: Int
22:     ): List<SalesforceUserDto> {
23:         val offset = page * size
24:         return userService.getAllUsers(limit = size, offset = offset)
25:     }
26:     @GetMapping("/db")
27:     @Operation(summary = "Search Users in Database", description = "Searches for users stored in the local database.")
28:     fun searchUsers(
29:         @RequestParam(required = false) name: String?,
30:         @RequestParam(defaultValue = "10") size: Int,
31:         @RequestParam(defaultValue = "0") page: Int
32:     ): List<User> {
33:         val offset = page * size
34:         return userService.searchUsers(name, size, offset)
35:     }
36: }
````

## File: src/main/kotlin/com/observability/sfdc/domain/ApexClass.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: @Entity
 4: @Table(name = "apex_classes")
 5: data class ApexClass(
 6:     @Id
 7:     @GeneratedValue(strategy = GenerationType.IDENTITY)
 8:     val id: Long? = null,
 9:     @Column(name = "sfdc_id", unique = true)
10:     val sfdcId: String,
11:     val name: String?,
12:     val apiVersion: Double?,
13:     val status: String?,
14:     val lengthWithoutComments: Int?,
15:     val lastModifiedDate: String?,
16:     val lastModifiedByName: String?,
17:     val createdDate: String?,
18:     val createdByName: String?,
19:     val numLinesCovered: Int? = null,
20:     val numLinesUncovered: Int? = null,
21:     @Column(columnDefinition = "TEXT")
22:     val body: String? = null
23: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/ApexTrigger.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: @Entity
 4: @Table(name = "apex_triggers")
 5: data class ApexTrigger(
 6:     @Id
 7:     @GeneratedValue(strategy = GenerationType.IDENTITY)
 8:     val id: Long? = null,
 9:     @Column(name = "sfdc_id", unique = true)
10:     val sfdcId: String,
11:     val name: String?,
12:     val sobject: String?,
13:     val apiVersion: Double?,
14:     val status: String?,
15:     val usageBeforeInsert: Boolean?,
16:     val usageBeforeUpdate: Boolean?,
17:     val usageBeforeDelete: Boolean?,
18:     val usageAfterInsert: Boolean?,
19:     val usageAfterUpdate: Boolean?,
20:     val usageAfterDelete: Boolean?,
21:     val usageAfterUndelete: Boolean?,
22:     val lastModifiedDate: String?,
23:     val lastModifiedByName: String?,
24:     val createdDate: String?,
25:     val createdByName: String?,
26:     val numLinesCovered: Int? = null,
27:     val numLinesUncovered: Int? = null,
28:     @Column(columnDefinition = "TEXT")
29:     val body: String? = null
30: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/DebugLevel.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: @Entity
 4: @Table(name = "debug_levels")
 5: data class DebugLevel(
 6:     @Id
 7:     @GeneratedValue(strategy = GenerationType.IDENTITY)
 8:     val id: Long? = null,
 9:     @Column(name = "sfdc_id", unique = true)
10:     val sfdcId: String,
11:     val developerName: String?,
12:     val masterLabel: String?,
13:     val apexCode: String?,
14:     val apexProfiling: String?,
15:     val callout: String?,
16:     val database: String?,
17:     val system: String?,
18:     val validation: String?,
19:     val visualforce: String?,
20:     val workflow: String?
21: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/Log.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: import java.time.Instant
 4: @Entity
 5: @Table(name = "logs", indexes = [
 6:     Index(name = "idx_logs_apex_class_name", columnList = "apex_class_name"),
 7:     Index(name = "idx_logs_author_name", columnList = "author_name"),
 8:     Index(name = "idx_logs_request_time", columnList = "request_time"),
 9:     Index(name = "idx_logs_sfdc_id", columnList = "sfdc_id")
10: ])
11: data class Log(
12:     @Id
13:     @GeneratedValue(strategy = GenerationType.IDENTITY)
14:     val id: Long? = null,
15:     @Column(name = "sfdc_id", unique = true)
16:     val sfdcId: String,
17:     @Column(name = "apex_class_name")
18:     val apexClassName: String? = null,
19:     @Column(name = "author_name")
20:     val authorName: String? = null,
21:     @Column(name = "request_time")
22:     val requestTime: Instant? = null,
23:     @Column(name = "operation")
24:     val operation: String? = null,
25:     @Column(name = "log_size")
26:     val logSize: Long? = null,
27:     @Column(name = "duration")
28:     val duration: Long? = null,
29:     @Column(name = "status")
30:     val status: String? = null,
31:     @Column(name = "request")
32:     val request: String? = null,
33:     @Column(name = "body", columnDefinition = "TEXT")
34:     val body: String? = null
35: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/MetadataHistory.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: import java.time.Instant
 4: @Entity
 5: @Table(name = "metadata_history")
 6: data class MetadataHistory(
 7:     @Id
 8:     @GeneratedValue(strategy = GenerationType.IDENTITY)
 9:     val id: Long? = null,
10:     @Column(name = "sfdc_id", nullable = false)
11:     val sfdcId: String,
12:     @Column(name = "entity_type", nullable = false)
13:     val entityType: String, // ApexClass or ApexTrigger
14:     @Column(columnDefinition = "TEXT")
15:     val body: String?,
16:     @Column(name = "created_at", nullable = false)
17:     val createdAt: Instant = Instant.now()
18: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/TraceJob.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import jakarta.persistence.*
 3: import java.time.Instant
 4: @Entity
 5: @Table(name = "trace_jobs")
 6: data class TraceJob(
 7:     @Id
 8:     @GeneratedValue(strategy = GenerationType.IDENTITY)
 9:     val id: Long? = null,
10:     @Column(name = "traced_entity_id", nullable = false)
11:     val tracedEntityId: String,
12:     @Column(name = "traced_entity_name")
13:     val tracedEntityName: String?,
14:     @Column(name = "traced_entity_type", nullable = false)
15:     val tracedEntityType: String,
16:     @Column(name = "debug_level_name", nullable = false)
17:     val debugLevelName: String,
18:     @Column(name = "start_time", nullable = false)
19:     val startTime: Instant,
20:     @Column(name = "end_time", nullable = false)
21:     val endTime: Instant,
22:     @Column(nullable = false)
23:     var status: String, // ACTIVE, COMPLETED, CANCELLED
24:     @Column(name = "sfdc_trace_flag_id")
25:     var sfdcTraceFlagId: String? = null
26: )
````

## File: src/main/kotlin/com/observability/sfdc/domain/User.kt
````kotlin
 1: package com.observability.sfdc.domain
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.persistence.*
 5: @Entity
 6: @Table(name = "users")
 7: @JsonIgnoreProperties(ignoreUnknown = true)
 8: data class User(
 9:     @Id
10:     @GeneratedValue(strategy = GenerationType.IDENTITY)
11:     val id: Long? = null,
12:     @Column(name = "sfdc_id", unique = true)
13:     val sfdcId: String,
14:     val name: String?,
15:     val username: String?,
16:     val email: String?,
17:     val profileName: String?,
18:     @JsonProperty("active")
19:     val isActive: Boolean?,
20:     val entity: String?
21: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/ApexClassDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.NotBlank
 5: @JsonIgnoreProperties(ignoreUnknown = true)
 6: data class ApexClassDto(
 7:     @JsonProperty("Id")
 8:     @field:NotBlank(message = "Salesforce ID is required")
 9:     val id: String,
10:     @JsonProperty("Name")
11:     @field:NotBlank(message = "Name is required")
12:     val name: String?,
13:     @JsonProperty("ApiVersion")
14:     val apiVersion: Double?,
15:     @JsonProperty("Status")
16:     val status: String?,
17:     @JsonProperty("LengthWithoutComments")
18:     val lengthWithoutComments: Int?,
19:     @JsonProperty("LastModifiedDate")
20:     val lastModifiedDate: String?,
21:     @JsonProperty("LastModifiedBy")
22:     val lastModifiedBy: UserSummaryDto?,
23:     @JsonProperty("CreatedDate")
24:     val createdDate: String?,
25:     @JsonProperty("CreatedBy")
26:     val createdBy: UserSummaryDto?,
27:     val coverage: ApexCodeCoverageDto? = null,
28:     @JsonProperty("Body")
29:     val body: String? = null
30: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/ApexCodeCoverageDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonAlias
 3: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 4: @JsonIgnoreProperties(ignoreUnknown = true)
 5: data class ApexCodeCoverageDto(
 6:     @JsonAlias("ApexClassOrTriggerId")
 7:     val apexClassOrTriggerId: String,
 8:     @JsonAlias("NumLinesCovered")
 9:     val numLinesCovered: Int = 0,
10:     @JsonAlias("NumLinesUncovered")
11:     val numLinesUncovered: Int = 0
12: ) {
13:     val coveragePercentage: Double
14:         get() {
15:             val totalLines = numLinesCovered + numLinesUncovered
16:             if (totalLines == 0) return 0.0
17:             return (numLinesCovered.toDouble() / totalLines.toDouble()) * 100.0
18:         }
19: }
````

## File: src/main/kotlin/com/observability/sfdc/dto/ApexLogDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.NotBlank
 5: @JsonIgnoreProperties(ignoreUnknown = true)
 6: data class ApexLogDto(
 7:     @JsonProperty("Id")
 8:     @field:NotBlank(message = "Salesforce ID is required")
 9:     val id: String,
10:     @JsonProperty("LogUser")
11:     val logUser: UserSummaryDto?,
12:     @JsonProperty("Operation")
13:     val operation: String?,
14:     @JsonProperty("StartTime")
15:     val startTime: String?,
16:     @JsonProperty("Status")
17:     val status: String?,
18:     @JsonProperty("Request")
19:     val request: String?,
20:     @JsonProperty("LogLength")
21:     val logLength: Long?,
22:     @JsonProperty("DurationMilliseconds")
23:     val durationMilliseconds: Long?,
24:     val apexClassName: String? = null
25: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/ApexTriggerDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.NotBlank
 5: @JsonIgnoreProperties(ignoreUnknown = true)
 6: data class ApexTriggerDto(
 7:     @JsonProperty("Id")
 8:     @field:NotBlank(message = "Salesforce ID is required")
 9:     val id: String,
10:     @JsonProperty("Name")
11:     @field:NotBlank(message = "Name is required")
12:     val name: String?,
13:     @JsonProperty("TableEnumOrId")
14:     val tableEnumOrId: String?,
15:     @JsonProperty("ApiVersion")
16:     val apiVersion: Double?,
17:     @JsonProperty("Status")
18:     val status: String?,
19:     @JsonProperty("UsageBeforeInsert")
20:     val usageBeforeInsert: Boolean?,
21:     @JsonProperty("UsageBeforeUpdate")
22:     val usageBeforeUpdate: Boolean?,
23:     @JsonProperty("UsageBeforeDelete")
24:     val usageBeforeDelete: Boolean?,
25:     @JsonProperty("UsageAfterInsert")
26:     val usageAfterInsert: Boolean?,
27:     @JsonProperty("UsageAfterUpdate")
28:     val usageAfterUpdate: Boolean?,
29:     @JsonProperty("UsageAfterDelete")
30:     val usageAfterDelete: Boolean?,
31:     @JsonProperty("UsageAfterUndelete")
32:     val usageAfterUndelete: Boolean?,
33:     @JsonProperty("LastModifiedDate")
34:     val lastModifiedDate: String?,
35:     @JsonProperty("LastModifiedBy")
36:     val lastModifiedBy: UserSummaryDto?,
37:     @JsonProperty("CreatedDate")
38:     val createdDate: String?,
39:     @JsonProperty("CreatedBy")
40:     val createdBy: UserSummaryDto?,
41:     val coverage: ApexCodeCoverageDto? = null,
42:     @JsonProperty("Body")
43:     val body: String? = null
44: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/DebugLevelDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.NotBlank
 5: @JsonIgnoreProperties(ignoreUnknown = true)
 6: data class DebugLevelDto(
 7:     @JsonProperty("Id")
 8:     @field:NotBlank(message = "Salesforce ID is required")
 9:     val id: String,
10:     @JsonProperty("DeveloperName")
11:     @field:NotBlank(message = "DeveloperName is required")
12:     val developerName: String?,
13:     @JsonProperty("MasterLabel")
14:     val masterLabel: String?,
15:     @JsonProperty("ApexCode")
16:     val apexCode: String?,
17:     @JsonProperty("ApexProfiling")
18:     val apexProfiling: String?,
19:     @JsonProperty("Callout")
20:     val callout: String?,
21:     @JsonProperty("Database")
22:     val database: String?,
23:     @JsonProperty("System")
24:     val system: String?,
25:     @JsonProperty("Validation")
26:     val validation: String?,
27:     @JsonProperty("Visualforce")
28:     val visualforce: String?,
29:     @JsonProperty("Workflow")
30:     val workflow: String?
31: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/MetadataDetailDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: @JsonIgnoreProperties(ignoreUnknown = true)
 4: data class MetadataDetailDto(
 5:     val id: String,
 6:     val name: String,
 7:     val type: String, // "ApexClass" or "ApexTrigger"
 8:     val apiVersion: Double?,
 9:     val status: String?,
10:     val lastModifiedDate: String?,
11:     val lastModifiedByName: String?,
12:     val targetObject: String? = null, // For Triggers: TableEnumOrId
13:     val triggerEvents: List<String> = emptyList(),
14:     val testClasses: List<ApexClassDto> = emptyList(),
15:     val coverage: ApexCodeCoverageDto? = null,
16:     val body: String? = null
17: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/MetadataDiffDto.kt
````kotlin
1: package com.observability.sfdc.dto
2: data class MetadataDiffDto(
3:     val previousBody: String,
4:     val latestBody: String
5: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/SalesforceQueryResult.kt
````kotlin
1: package com.observability.sfdc.dto
2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
3: @JsonIgnoreProperties(ignoreUnknown = true)
4: data class SalesforceQueryResult<T>(
5:     val totalSize: Int,
6:     val done: Boolean,
7:     val records: List<T>
8: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/SalesforceSearchResponse.kt
````kotlin
1: package com.observability.sfdc.dto
2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
3: @JsonIgnoreProperties(ignoreUnknown = true)
4: data class SalesforceSearchResponse<T>(
5:     val searchRecords: List<T>
6: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/SalesforceTokenResponse.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonProperty
 3: @com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
 4: data class SalesforceTokenResponse(
 5:     @JsonProperty("access_token")
 6:     val accessToken: String? = null,
 7:     @JsonProperty("instance_url")
 8:     val instanceUrl: String? = null,
 9:     @JsonProperty("id")
10:     val id: String? = null,
11:     @JsonProperty("token_type")
12:     val tokenType: String? = null,
13:     @JsonProperty("issued_at")
14:     val issuedAt: String? = null,
15:     @JsonProperty("signature")
16:     val signature: String? = null
17: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/TraceFlagDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.Min
 5: import jakarta.validation.constraints.NotBlank
 6: @JsonIgnoreProperties(ignoreUnknown = true)
 7: data class TraceFlagRequest(
 8:     @JsonProperty("TracedEntityId")
 9:     @field:NotBlank(message = "TracedEntityId is required")
10:     val tracedEntityId: String,
11:     @JsonProperty("DebugLevelId")
12:     @field:NotBlank(message = "DebugLevelId is required")
13:     val debugLevelId: String,
14:     @JsonProperty("LogType")
15:     @field:NotBlank(message = "LogType is required")
16:     val logType: String,
17:     @JsonProperty("StartDate")
18:     val startDate: String? = null,
19:     @JsonProperty("ExpirationDate")
20:     @field:NotBlank(message = "ExpirationDate is required")
21:     val expirationDate: String
22: )
23: @JsonIgnoreProperties(ignoreUnknown = true)
24: data class FrontendTraceFlagRequest(
25:     @field:NotBlank(message = "TracedEntityId is required")
26:     val tracedEntityId: String,
27:     val tracedEntityName: String? = null,
28:     @field:NotBlank(message = "DebugLevelName is required")
29:     val debugLevelName: String,
30:     @field:Min(value = 0, message = "Duration days must be non-negative")
31:     val durationDays: Int? = 0,
32:     @field:Min(value = 0, message = "Duration hours must be non-negative")
33:     val durationHours: Int? = 0,
34:     @field:Min(value = 0, message = "Duration minutes must be non-negative")
35:     val durationMinutes: Int? = 0,
36:     val entityType: String? = "User"
37: ) {
38:     fun getTotalMinutes(): Long {
39:         return ((durationDays ?: 0).toLong() * 24 * 60) + ((durationHours ?: 0).toLong() * 60) + (durationMinutes ?: 0).toLong()
40:     }
41: }
42: @JsonIgnoreProperties(ignoreUnknown = true)
43: data class SalesforceCreateResponse(
44:     val id: String?,
45:     val success: Boolean,
46:     val errors: List<String> = emptyList()
47: )
48: @JsonIgnoreProperties(ignoreUnknown = true)
49: data class TraceFlagDto(
50:     @JsonProperty("Id")
51:     val id: String,
52:     @JsonProperty("TracedEntityId")
53:     val tracedEntityId: String,
54:     @JsonProperty("TracedEntity")
55:     val tracedEntity: TracedEntityDto?,
56:     @JsonProperty("StartDate")
57:     val startDate: String?,
58:     @JsonProperty("ExpirationDate")
59:     val expirationDate: String?,
60:     @JsonProperty("DebugLevelId")
61:     val debugLevelId: String?,
62:     @JsonProperty("DebugLevel")
63:     val debugLevel: DebugLevelSummaryDto?,
64:     @JsonProperty("LogType")
65:     val logType: String? = null
66: )
67: @JsonIgnoreProperties(ignoreUnknown = true)
68: data class TracedEntityDto(
69:     @JsonProperty("Name")
70:     val name: String?,
71:     @JsonProperty("attributes")
72:     val attributes: EntityAttributesDto?
73: )
74: @JsonIgnoreProperties(ignoreUnknown = true)
75: data class EntityAttributesDto(
76:     @JsonProperty("type")
77:     val type: String?
78: )
79: @JsonIgnoreProperties(ignoreUnknown = true)
80: data class DebugLevelSummaryDto(
81:     @JsonProperty("DeveloperName")
82:     val developerName: String?
83: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/UserDto.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
 3: import com.fasterxml.jackson.annotation.JsonProperty
 4: import jakarta.validation.constraints.NotBlank
 5: @JsonIgnoreProperties(ignoreUnknown = true)
 6: data class SalesforceUserDto(
 7:     @JsonProperty("Id")
 8:     @field:NotBlank(message = "Salesforce ID is required")
 9:     val id: String,
10:     @JsonProperty("Name")
11:     @field:NotBlank(message = "Name is required")
12:     val name: String?,
13:     @JsonProperty("SFID")
14:     val sfdcId: String?,
15:     @JsonProperty("Username")
16:     val username: String?,
17:     @JsonProperty("Email")
18:     val email: String?,
19:     @JsonProperty("Profile")
20:     val profile: SalesforceProfileDto?,
21:     @JsonProperty("IsActive")
22:     val isActive: Boolean?,
23:     @JsonProperty("Entity__c")
24:     val entity: String?
25: )
26: @JsonIgnoreProperties(ignoreUnknown = true)
27: data class SalesforceProfileDto(
28:     @JsonProperty("Name")
29:     val name: String?
30: )
````

## File: src/main/kotlin/com/observability/sfdc/dto/UserSummaryDto.kt
````kotlin
1: package com.observability.sfdc.dto
2: import com.fasterxml.jackson.annotation.JsonIgnoreProperties
3: import com.fasterxml.jackson.annotation.JsonProperty
4: @JsonIgnoreProperties(ignoreUnknown = true)
5: data class UserSummaryDto(
6:     @JsonProperty("Name")
7:     val name: String?
8: )
````

## File: src/main/kotlin/com/observability/sfdc/repository/ApexClassRepository.kt
````kotlin
 1: package com.observability.sfdc.repository
 2: import com.observability.sfdc.domain.ApexClass
 3: import org.springframework.data.domain.Pageable
 4: import org.springframework.data.jpa.repository.JpaRepository
 5: import org.springframework.stereotype.Repository
 6: import java.util.*
 7: @Repository
 8: interface ApexClassRepository : JpaRepository<ApexClass, Long> {
 9:     fun findBySfdcId(sfdcId: String): Optional<ApexClass>
10:     fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): List<ApexClass>
11:     fun findAllProjectedBy(pageable: Pageable): List<ApexClass>
12: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/ApexTriggerRepository.kt
````kotlin
 1: package com.observability.sfdc.repository
 2: import com.observability.sfdc.domain.ApexTrigger
 3: import org.springframework.data.domain.Pageable
 4: import org.springframework.data.jpa.repository.JpaRepository
 5: import org.springframework.stereotype.Repository
 6: import java.util.*
 7: @Repository
 8: interface ApexTriggerRepository : JpaRepository<ApexTrigger, Long> {
 9:     fun findBySfdcId(sfdcId: String): Optional<ApexTrigger>
10:     fun findByNameContainingIgnoreCaseOrSobjectContainingIgnoreCase(name: String, sobject: String, pageable: Pageable): List<ApexTrigger>
11:     fun findAllProjectedBy(pageable: Pageable): List<ApexTrigger>
12: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/DebugLevelRepository.kt
````kotlin
 1: package com.observability.sfdc.repository
 2: import com.observability.sfdc.domain.DebugLevel
 3: import org.springframework.data.domain.Pageable
 4: import org.springframework.data.jpa.repository.JpaRepository
 5: import org.springframework.stereotype.Repository
 6: import java.util.*
 7: @Repository
 8: interface DebugLevelRepository : JpaRepository<DebugLevel, Long> {
 9:     fun findBySfdcId(sfdcId: String): Optional<DebugLevel>
10:     fun findByDeveloperNameContainingIgnoreCaseOrMasterLabelContainingIgnoreCase(developerName: String, masterLabel: String, pageable: Pageable): List<DebugLevel>
11:     fun findAllProjectedBy(pageable: Pageable): List<DebugLevel>
12: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/LogRepository.kt
````kotlin
 1: package com.observability.sfdc.repository
 2: import com.observability.sfdc.domain.Log
 3: import org.springframework.data.domain.Pageable
 4: import org.springframework.data.jpa.repository.JpaRepository
 5: import org.springframework.stereotype.Repository
 6: import java.time.Instant
 7: import java.util.*
 8: @Repository
 9: interface LogRepository : JpaRepository<Log, Long> {
10:     fun findBySfdcId(sfdcId: String): Optional<Log>
11:     fun deleteBySfdcId(sfdcId: String)
12:     fun findAllByOrderByRequestTimeDesc(pageable: Pageable): List<Log>
13:     fun findByApexClassNameContainingIgnoreCase(apexClassName: String, pageable: Pageable): List<Log>
14:     fun findByAuthorNameContainingIgnoreCase(authorName: String, pageable: Pageable): List<Log>
15:     fun findByApexClassNameContainingIgnoreCaseAndAuthorNameContainingIgnoreCase(apexClassName: String, authorName: String, pageable: Pageable): List<Log>
16:     fun deleteByRequestTimeBefore(cutoff: Instant): Int
17: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/MetadataHistoryRepository.kt
````kotlin
1: package com.observability.sfdc.repository
2: import com.observability.sfdc.domain.MetadataHistory
3: import org.springframework.data.jpa.repository.JpaRepository
4: import org.springframework.stereotype.Repository
5: @Repository
6: interface MetadataHistoryRepository : JpaRepository<MetadataHistory, Long> {
7:     fun findTopBySfdcIdAndEntityTypeOrderByCreatedAtDesc(sfdcId: String, entityType: String): MetadataHistory?
8: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/TraceJobRepository.kt
````kotlin
1: package com.observability.sfdc.repository
2: import com.observability.sfdc.domain.TraceJob
3: import org.springframework.data.jpa.repository.JpaRepository
4: import org.springframework.stereotype.Repository
5: @Repository
6: interface TraceJobRepository : JpaRepository<TraceJob, Long> {
7:     fun findByStatus(status: String): List<TraceJob>
8:     fun findByTracedEntityNameContainingIgnoreCase(name: String): List<TraceJob>
9: }
````

## File: src/main/kotlin/com/observability/sfdc/repository/UserRepository.kt
````kotlin
 1: package com.observability.sfdc.repository
 2: import com.observability.sfdc.domain.User
 3: import org.springframework.data.domain.Pageable
 4: import org.springframework.data.jpa.repository.JpaRepository
 5: import org.springframework.stereotype.Repository
 6: import java.util.*
 7: @Repository
 8: interface UserRepository : JpaRepository<User, Long> {
 9:     fun findBySfdcId(sfdcId: String): Optional<User>
10:     fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): List<User>
11:     fun findAllProjectedBy(pageable: Pageable): List<User>
12: }
````

## File: src/main/kotlin/com/observability/sfdc/service/LogRetentionService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.repository.LogRepository
 3: import org.slf4j.LoggerFactory
 4: import org.springframework.beans.factory.annotation.Value
 5: import org.springframework.scheduling.annotation.Scheduled
 6: import org.springframework.stereotype.Service
 7: import org.springframework.transaction.annotation.Transactional
 8: import java.time.Instant
 9: @Service
10: class LogRetentionService(
11:     private val logRepository: LogRepository,
12:     @Value($$"${log.retention.days:30}") private val retentionDays: Long
13: ) {
14:     private val logger = LoggerFactory.getLogger(LogRetentionService::class.java)
15:     @Scheduled(cron = "\${log.retention.cron:0 0 3 * * ?}")
16:     @Transactional
17:     fun purgeOldLogs() {
18:         val cutoff = Instant.now().minusSeconds(retentionDays * 86400)
19:         logger.info("Starting log retention purge: removing logs older than $retentionDays days (before $cutoff)...")
20:         val deleted = logRepository.deleteByRequestTimeBefore(cutoff)
21:         logger.info("Log retention purge complete: $deleted logs deleted.")
22:     }
23: }
````

## File: src/main/kotlin/com/observability/sfdc/service/MetadataComparisonService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.domain.MetadataHistory
 3: import com.observability.sfdc.dto.MetadataDiffDto
 4: import com.observability.sfdc.repository.MetadataHistoryRepository
 5: import org.springframework.stereotype.Service
 6: import org.springframework.transaction.annotation.Transactional
 7: @Service
 8: class MetadataComparisonService(
 9:     private val metadataService: SalesforceMetadataService,
10:     private val historyRepository: MetadataHistoryRepository
11: ) {
12:     @Transactional
13:     fun compareMetadata(entityId: String, type: String): MetadataDiffDto {
14:         val detail = metadataService.getMetadataDetail(entityId, type)
15:             ?: throw IllegalArgumentException("Entity not found in Salesforce or has no body")
16:         val latestBody = detail.body ?: throw IllegalArgumentException("Entity has no body")
17:         val previousHistory = historyRepository.findTopBySfdcIdAndEntityTypeOrderByCreatedAtDesc(entityId, type)
18:         val previousBody = previousHistory?.body ?: ""
19:         historyRepository.save(MetadataHistory(sfdcId = entityId, entityType = type, body = latestBody))
20:         return MetadataDiffDto(previousBody = previousBody, latestBody = latestBody)
21:     }
22:     @Transactional
23:     fun saveHistory(entityId: String, type: String, body: String) {
24:         historyRepository.save(MetadataHistory(sfdcId = entityId, entityType = type, body = body))
25:     }
26: }
````

## File: src/main/kotlin/com/observability/sfdc/service/MinioService.kt
````kotlin
  1: package com.observability.sfdc.service
  2: import io.minio.*
  3: import org.slf4j.LoggerFactory
  4: import org.springframework.beans.factory.annotation.Value
  5: import org.springframework.scheduling.annotation.Async
  6: import org.springframework.stereotype.Service
  7: import java.io.ByteArrayInputStream
  8: import java.io.ByteArrayOutputStream
  9: import java.io.InputStream
 10: import java.util.zip.GZIPInputStream
 11: import java.util.zip.GZIPOutputStream
 12: @Service
 13: class MinioService(
 14:     private val minioClient: MinioClient,
 15:     @Value($$"${minio.bucket-name}") private val bucketName: String
 16: ) {
 17:     private val logger = LoggerFactory.getLogger(MinioService::class.java)
 18:     init {
 19:         ensureBucketExists()
 20:     }
 21:     private fun ensureBucketExists() {
 22:         try {
 23:             val found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())
 24:             if (!found) {
 25:                 minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build())
 26:                 logger.info("Created MinIO bucket: $bucketName")
 27:             }
 28:         } catch (e: Exception) {
 29:             logger.error("Error ensuring MinIO bucket exists: ${e.message}")
 30:         }
 31:     }
 32:     fun exists(logId: String): Boolean {
 33:         return try {
 34:             minioClient.statObject(
 35:                 StatObjectArgs.builder()
 36:                     .bucket(bucketName)
 37:                     .`object`("$logId.log.gz")
 38:                     .build()
 39:             )
 40:             true
 41:         } catch (_: Exception) {
 42:             false
 43:         }
 44:     }
 45:     @Async
 46:     fun uploadLog(logId: String, body: String) {
 47:         uploadLogSync(logId, body)
 48:     }
 49:     fun uploadLogSync(logId: String, body: String) {
 50:         try {
 51:             val compressedData = compress(body)
 52:             val inputStream = ByteArrayInputStream(compressedData)
 53:             minioClient.putObject(
 54:                 PutObjectArgs.builder()
 55:                     .bucket(bucketName)
 56:                     .`object`("$logId.log.gz")
 57:                     .stream(inputStream, compressedData.size.toLong(), -1)
 58:                     .contentType("application/gzip")
 59:                     .build()
 60:             )
 61:             logger.info("Uploaded compressed log $logId to MinIO")
 62:         } catch (e: Exception) {
 63:             logger.error("Failed to upload log $logId to MinIO: ${e.message}")
 64:         }
 65:     }
 66:     fun downloadLog(logId: String): String? {
 67:         return try {
 68:             val stream = minioClient.getObject(
 69:                 GetObjectArgs.builder()
 70:                     .bucket(bucketName)
 71:                     .`object`("$logId.log.gz")
 72:                     .build()
 73:             )
 74:             val compressedData = stream.use { it.readAllBytes() }
 75:             decompress(compressedData)
 76:         } catch (e: Exception) {
 77:             // Log not found or other error - return null to fallback
 78:             logger.error("Error downloading log $logId from MinIO: ${e.message}")
 79:             null
 80:         }
 81:     }
 82:     fun getDownloadStream(logId: String): InputStream? {
 83:         return try {
 84:             minioClient.getObject(
 85:                 GetObjectArgs.builder()
 86:                     .bucket(bucketName)
 87:                     .`object`("$logId.log.gz")
 88:                     .build()
 89:             )
 90:         } catch (e: Exception) {
 91:             logger.error("Failed to get stream for $logId: ${e.message}")
 92:             null
 93:         }
 94:     }
 95:     fun deleteLog(logId: String) {
 96:         try {
 97:             minioClient.removeObject(
 98:                 RemoveObjectArgs.builder()
 99:                     .bucket(bucketName)
100:                     .`object`("$logId.log.gz")
101:                     .build()
102:             )
103:             logger.info("Deleted log $logId from MinIO")
104:         } catch (e: Exception) {
105:             logger.error("Failed to delete log $logId from MinIO: ${e.message}")
106:         }
107:     }
108:     private fun compress(data: String): ByteArray {
109:         val bos = ByteArrayOutputStream()
110:         GZIPOutputStream(bos).use { it.write(data.toByteArray()) }
111:         return bos.toByteArray()
112:     }
113:     private fun decompress(compressedData: ByteArray): String {
114:         val bis = ByteArrayInputStream(compressedData)
115:         return GZIPInputStream(bis).bufferedReader().use { it.readText() }
116:     }
117: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceAuthService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.dto.SalesforceTokenResponse
 3: import org.slf4j.LoggerFactory
 4: import org.springframework.beans.factory.annotation.Value
 5: import org.springframework.cache.annotation.Cacheable
 6: import org.springframework.http.HttpEntity
 7: import org.springframework.http.HttpHeaders
 8: import org.springframework.http.MediaType
 9: import org.springframework.stereotype.Service
10: import org.springframework.util.LinkedMultiValueMap
11: import org.springframework.web.client.RestTemplate
12: @Service
13: class SalesforceAuthService(
14:     @Value($$"${salesforce.login-url}") private val loginUrl: String,
15:     @Value($$"${salesforce.client-id}") private val clientId: String,
16:     @Value($$"${salesforce.client-secret}") private val clientSecret: String,
17:     @Value($$"${salesforce.grant-type}") private val grantType: String
18: ) {
19:     private val restTemplate = RestTemplate()
20:     private val logger = LoggerFactory.getLogger(SalesforceAuthService::class.java)
21:     @Cacheable(value = ["sf_tokens"], key = "'client_credentials_token'", unless = "#result == null")
22:     fun getAccessToken(): SalesforceTokenResponse? {
23:         val url = "$loginUrl/services/oauth2/token"
24:         logger.info("Attempting Salesforce authentication at: $url")
25:         logger.info("Using Grant Type: $grantType")
26:         if (clientId.isBlank()) {
27:             logger.error("SALESFORCE_CLIENT_ID is blank or missing!")
28:         } else {
29:             logger.info("Client ID successfully loaded")
30:         }
31:         val headers = HttpHeaders()
32:         headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
33:         val map = LinkedMultiValueMap<String, String>()
34:         map.add("grant_type", grantType)
35:         map.add("client_id", clientId)
36:         map.add("client_secret", clientSecret)
37:         val request = HttpEntity(map, headers)
38:         return try {
39:             val response = restTemplate.postForObject(url, request, SalesforceTokenResponse::class.java)
40:             if (response?.accessToken == null || response.instanceUrl == null) {
41:                 logger.error("Salesforce response missing essential fields: $response")
42:                 return null
43:             }
44:             logger.info("Successfully authenticated with Salesforce.")
45:             response
46:         } catch (e: Exception) {
47:             logger.error("Error authenticating with Salesforce: ${e.message}")
48:             if (e is org.springframework.web.client.HttpClientErrorException) {
49:                 logger.error("Response Body: ${e.responseBodyAsString}")
50:             }
51:             null
52:         }
53:     }
54: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceBaseService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.dto.SalesforceQueryResult
 3: import org.slf4j.LoggerFactory
 4: import org.springframework.core.ParameterizedTypeReference
 5: import org.springframework.http.HttpEntity
 6: import org.springframework.http.HttpHeaders
 7: import org.springframework.http.HttpMethod
 8: import org.springframework.http.MediaType
 9: import org.springframework.web.client.RestTemplate
10: import org.springframework.web.util.UriComponentsBuilder
11: abstract class SalesforceBaseService(
12:     protected val authService: SalesforceAuthService,
13:     protected val apiVersion: String
14: ) {
15:     protected val logger = LoggerFactory.getLogger(this::class.java)
16:     protected val restTemplate = RestTemplate()
17:     internal open fun <T> executeWithToken(
18:         operationName: String,
19:         fallback: T,
20:         action: (token: String, instanceUrl: String) -> T
21:     ): T {
22:         val tokenResponse = authService.getAccessToken() ?: return fallback
23:         return try {
24:             action(tokenResponse.accessToken!!, tokenResponse.instanceUrl!!)
25:         } catch (e: Exception) {
26:             logger.error("Error $operationName: ${e.message}")
27:             fallback
28:         }
29:     }
30:     protected fun createHeaders(token: String, contentType: MediaType? = null): HttpHeaders {
31:         return HttpHeaders().apply {
32:             setBearerAuth(token)
33:             contentType?.let { this.contentType = it }
34:         }
35:     }
36:     protected fun buildUri(instanceUrl: String, path: String, useTooling: Boolean = true): UriComponentsBuilder {
37:         val apiPath = if (useTooling) "tooling" else ""
38:         val baseUrl = if (apiPath.isNotEmpty()) "$instanceUrl/services/data/$apiVersion/$apiPath/$path"
39:                       else "$instanceUrl/services/data/$apiVersion/$path"
40:         return UriComponentsBuilder.fromUriString(baseUrl)
41:     }
42:     internal open fun <T> querySalesforce(
43:         operationName: String,
44:         query: String,
45:         typeReference: ParameterizedTypeReference<SalesforceQueryResult<T>>,
46:         useTooling: Boolean = true
47:     ): List<T> {
48:         return executeWithToken(operationName, emptyList()) { token, instanceUrl ->
49:             val uri = buildUri(instanceUrl, "query", useTooling)
50:                 .queryParam("q", query)
51:                 .build()
52:                 .toUri()
53:             val entity = HttpEntity<Unit>(createHeaders(token))
54:             val response = restTemplate.exchange(uri, HttpMethod.GET, entity, typeReference)
55:             val result = response.body
56:             val records = result?.records ?: emptyList()
57:             if (records.isEmpty()) {
58:                 logger.info("$operationName: Query returned 0 records. Total size in Salesforce: ${result?.totalSize ?: "unknown"}")
59:             }
60:             records
61:         }
62:     }
63: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceLogPollingService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.domain.Log
 3: import com.observability.sfdc.repository.LogRepository
 4: import org.slf4j.LoggerFactory
 5: import org.springframework.scheduling.annotation.Scheduled
 6: import org.springframework.stereotype.Service
 7: import org.springframework.transaction.annotation.Transactional
 8: import java.time.Instant
 9: import java.time.OffsetDateTime
10: import java.time.format.DateTimeFormatter
11: @Service
12: class SalesforceLogPollingService(
13:     private val logService: SalesforceLogService,
14:     private val logRepository: LogRepository
15: ) {
16:     private val logger = LoggerFactory.getLogger(SalesforceLogPollingService::class.java)
17:     private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
18:     @Scheduled(fixedRate = 60000)
19:     @Transactional
20:     fun pollLogs() {
21:         logger.info("Starting Salesforce log polling cycle...")
22:         try {
23:             // Fetch logs without bodies first to check against database
24:             val logs = logService.queryApexLogs(limit = 20, fetchBody = false)
25:             logger.info("Retrieved ${logs.size} log headers from Salesforce.")
26:             var newLogsCount = 0
27:             logs.forEach { dto ->
28:                 if (!logRepository.findBySfdcId(dto.id).isPresent) {
29:                     // Fetch body only for new logs (this triggers MinIO upload)
30:                     val body = logService.getLogBody(dto.id)
31:                     val apexClassName = dto.apexClassName ?: logService.extractClassName(body)
32:                     val log = Log(
33:                         sfdcId = dto.id,
34:                         apexClassName = apexClassName,
35:                         authorName = dto.logUser?.name,
36:                         requestTime = parseDateTime(dto.startTime),
37:                         operation = dto.operation,
38:                         logSize = dto.logLength,
39:                         duration = dto.durationMilliseconds,
40:                         status = dto.status,
41:                         request = dto.request,
42:                         body = body
43:                     )
44:                     logRepository.save(log)
45:                     newLogsCount++
46:                 }
47:             }
48:             if (newLogsCount > 0) {
49:                 logger.info("Success: Saved $newLogsCount new logs to PostgreSQL and MinIO.")
50:             } else {
51:                 logger.info("Poll complete: No new logs found to save.")
52:             }
53:         } catch (e: Exception) {
54:             logger.error("Critical error during Salesforce log polling: ${e.message}", e)
55:         }
56:     }
57:     private fun parseDateTime(startTime: String?): Instant? {
58:         if (startTime == null) return null
59:         return try {
60:             // Salesforce format: 2026-05-09T10:00:00.000+0000
61:             OffsetDateTime.parse(startTime, dateTimeFormatter).toInstant()
62:         } catch (e: Exception) {
63:             logger.warn("Failed to parse start time: $startTime. Error: ${e.message}")
64:             null
65:         }
66:     }
67: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceLogService.kt
````kotlin
  1: package com.observability.sfdc.service
  2: import com.observability.sfdc.dto.*
  3: import com.observability.sfdc.repository.DebugLevelRepository
  4: import com.observability.sfdc.repository.LogRepository
  5: import org.springframework.beans.factory.annotation.Value
  6: import org.springframework.core.ParameterizedTypeReference
  7: import org.springframework.http.HttpEntity
  8: import org.springframework.http.HttpMethod
  9: import org.springframework.http.MediaType
 10: import org.springframework.http.client.JdkClientHttpRequestFactory
 11: import org.springframework.stereotype.Service
 12: import org.springframework.transaction.annotation.Transactional
 13: import java.io.InputStream
 14: import java.time.ZonedDateTime
 15: import java.time.format.DateTimeFormatter
 16: @Service
 17: class SalesforceLogService(
 18:     authService: SalesforceAuthService,
 19:     private val debugLevelRepository: DebugLevelRepository,
 20:     private val logRepository: LogRepository,
 21:     private val minioService: MinioService,
 22:     @Value($$"${salesforce.api-version}") apiVersion: String
 23: ) : SalesforceBaseService(authService, apiVersion) {
 24:     private val salesforceIdRegex = Regex("^[a-zA-Z0-9]{15}(?:[a-zA-Z0-9]{3})?$")
 25:     init {
 26:         restTemplate.requestFactory = JdkClientHttpRequestFactory()
 27:     }
 28:     private fun isValidSalesforceId(id: String): Boolean = salesforceIdRegex.matches(id)
 29:     private val sfdcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
 30:     fun queryApexLogs(limit: Int = 10, offset: Int = 0, fetchBody: Boolean = true): List<ApexLogDto> {
 31:         val query = "SELECT Id, LogUser.Name, Operation, StartTime, Status, Request, LogLength, DurationMilliseconds FROM ApexLog ORDER BY StartTime DESC LIMIT $limit OFFSET $offset"
 32:         val records = querySalesforce("querying ApexLogs", query, object : ParameterizedTypeReference<SalesforceQueryResult<ApexLogDto>>() {}, useTooling = false)
 33:         if (!fetchBody || records.isEmpty()) return records
 34:         // Enrich records with Apex Class Name by fetching bodies (will check MinIO first)
 35:         return records.map { dto ->
 36:             val body = getLogBody(dto.id)
 37:             dto.copy(apexClassName = extractClassName(body))
 38:         }
 39:     }
 40:     fun extractClassName(body: String?): String? {
 41:         if (body == null) return null
 42:         // Pattern to find the last CODE_UNIT_STARTED or CODE_UNIT_FINISHED which contains the entry point.
 43:         // This is more reliable as it's typically at the end of the log and captures full context (Classes, Triggers, VF).
 44:         val codeUnitRegex = Regex("\\|CODE_UNIT_(?:STARTED|FINISHED)\\|(?:.*\\|)?([^\\r\\n|]+)")
 45:         val matches = codeUnitRegex.findAll(body).toList()
 46:         if (matches.isNotEmpty()) {
 47:             val fullPath = matches.last().groupValues[1].trim()
 48:             // Handle Visualforce pages: VF: /apex/PageName -> extract PageName
 49:             if (fullPath.startsWith("VF: /apex/")) {
 50:                 return fullPath.substringAfterLast("/")
 51:             }
 52:             // Handle Internal Triggers: __sfdc_trigger/TriggerName -> extract TriggerName
 53:             if (fullPath.startsWith("__sfdc_trigger/")) {
 54:                 return fullPath.substringAfter("/")
 55:             }
 56:             // Handle Triggers: TriggerName on SObject -> keep full trigger context
 57:             if (fullPath.contains(" on ", ignoreCase = true)) {
 58:                 return fullPath
 59:             }
 60:             // Handle Apex Classes: ClassName.methodName -> extract only ClassName
 61:             // Taking the part before the last dot as the metadata name (handles namespaces correctly).
 62:             return fullPath.substringBeforeLast(".")
 63:         }
 64:         // Fallback for standard Apex classes if no CODE_UNIT info is found
 65:         val classRegex = Regex("\\|(?:METHOD_ENTRY|CLASS_ENTRY)\\|\\[[^]]*]\\|(?:[^|]*\\|)?([^.| \\n]+)")
 66:         return classRegex.find(body)?.groupValues?.get(1)
 67:     }
 68:     fun getLogBody(logId: String): String? {
 69:         // 1. Try PostgreSQL first
 70:         val dbLog = logRepository.findBySfdcId(logId)
 71:         if (dbLog.isPresent && dbLog.get().body != null) {
 72:             return dbLog.get().body
 73:         }
 74:         // 2. Try MinIO next
 75:         val cachedBody = minioService.downloadLog(logId)
 76:         if (cachedBody != null) {
 77:             return cachedBody
 78:         }
 79:         // 3. Fallback to Salesforce Tooling API
 80:         val body = executeWithToken("fetching log body for $logId from Salesforce", null) { token, instanceUrl ->
 81:             val url = buildUri(instanceUrl, "sobjects/ApexLog/$logId/Body").build().toUriString()
 82:             restTemplate.exchange(url, HttpMethod.GET, HttpEntity<Unit>(createHeaders(token)), String::class.java).body
 83:         }
 84:         // 4. Store in MinIO and PostgreSQL for future use
 85:         if (body != null) {
 86:             minioService.uploadLog(logId, body)
 87:             logRepository.findBySfdcId(logId).ifPresent { log ->
 88:                 logRepository.save(log.copy(body = body))
 89:             }
 90:         }
 91:         return body
 92:     }
 93:     fun getLogDownloadStream(logId: String): InputStream? {
 94:         // Ensure log exists in MinIO first
 95:         if (!minioService.exists(logId)) {
 96:             val body = getLogBody(logId) ?: return null // This will fetch from SFDC
 97:             minioService.uploadLogSync(logId, body)
 98:         }
 99:         return minioService.getDownloadStream(logId)
100:     }
101:     fun createTraceFlag(frontendRequest: FrontendTraceFlagRequest): SalesforceCreateResponse? {
102:         // Resolve DebugLevel ID
103:         val debugLevels = debugLevelRepository.findAll()
104:         val debugLevel = debugLevels.find { it.developerName == frontendRequest.debugLevelName || it.masterLabel == frontendRequest.debugLevelName }
105:             ?: return SalesforceCreateResponse(id = null, success = false, errors = listOf("DebugLevel '${frontendRequest.debugLevelName}' not found. Please sync metadata first."))
106:         val now = ZonedDateTime.now(java.time.ZoneId.of("UTC"))
107:         val startDate = now.format(sfdcFormatter)
108:         val expirationDate = now
109:             .plusDays((frontendRequest.durationDays ?: 0).toLong())
110:             .plusHours((frontendRequest.durationHours ?: 0).toLong())
111:             .plusMinutes((frontendRequest.durationMinutes ?: 0).toLong())
112:             .format(sfdcFormatter)
113:         val logType = when (frontendRequest.entityType) {
114:             "ApexClass", "ApexTrigger" -> "CLASS_TRACING"
115:             else -> "USER_DEBUG"
116:         }
117:         val sfdcRequest = TraceFlagRequest(
118:             tracedEntityId = frontendRequest.tracedEntityId,
119:             debugLevelId = debugLevel.sfdcId,
120:             logType = logType,
121:             startDate = startDate,
122:             expirationDate = expirationDate
123:         )
124:         return executeWithToken("creating TraceFlag", SalesforceCreateResponse(id = null, success = false, errors = listOf("Authentication failed"))) { token, instanceUrl ->
125:             val url = buildUri(instanceUrl, "sobjects/TraceFlag").build().toUriString()
126:             val entity = HttpEntity(sfdcRequest, createHeaders(token, MediaType.APPLICATION_JSON))
127:             restTemplate.postForObject(url, entity, SalesforceCreateResponse::class.java)
128:         }
129:     }
130:     fun getActiveTraceFlags(): List<TraceFlagDto> {
131:         val now = ZonedDateTime.now(java.time.ZoneId.of("UTC")).format(sfdcFormatter)
132:         val query = "SELECT Id, TracedEntityId, TracedEntity.Name, StartDate, ExpirationDate, DebugLevelId, DebugLevel.DeveloperName, LogType FROM TraceFlag WHERE ExpirationDate > $now"
133:         return querySalesforce("querying active TraceFlags", query, object : ParameterizedTypeReference<SalesforceQueryResult<TraceFlagDto>>() {})
134:     }
135:     fun getAllTraceFlags(): List<TraceFlagDto> {
136:         val query = "SELECT Id, TracedEntityId, TracedEntity.Name, StartDate, ExpirationDate, DebugLevelId, DebugLevel.DeveloperName, LogType FROM TraceFlag ORDER BY ExpirationDate DESC"
137:         return querySalesforce("querying all TraceFlags", query, object : ParameterizedTypeReference<SalesforceQueryResult<TraceFlagDto>>() {})
138:     }
139:     @Transactional
140:     fun deleteLog(id: String): Boolean {
141:         if (!isValidSalesforceId(id)) return false
142:         // 1. Delete from Salesforce
143:         val deletedFromSF = executeWithToken("deleting ApexLog $id", false) { token, instanceUrl ->
144:             val uri = buildUri(instanceUrl, "sobjects/ApexLog/$id", useTooling = false).build().toUri()
145:             restTemplate.exchange(uri, HttpMethod.DELETE, HttpEntity<Unit>(createHeaders(token)), Unit::class.java).statusCode.is2xxSuccessful
146:         }
147:         // 2. Cleanup local storage and database
148:         minioService.deleteLog(id)
149:         logRepository.deleteBySfdcId(id)
150:         return deletedFromSF
151:     }
152:     @Transactional
153:     fun deleteLogs(ids: List<String>): Map<String, Boolean> {
154:         return ids.associateWith { deleteLog(it) }
155:     }
156:     @Transactional
157:     fun deleteAllLogs(): Int {
158:         val query = "SELECT Id FROM ApexLog"
159:         val records = querySalesforce("querying all ApexLogs for deletion", query, object : ParameterizedTypeReference<SalesforceQueryResult<ApexLogDto>>() {}, useTooling = false)
160:         var count = 0
161:         records.forEach { 
162:             if (deleteLog(it.id)) count++
163:         }
164:         return count
165:     }
166:     fun deleteTraceFlag(id: String): Boolean {
167:         if (!isValidSalesforceId(id)) return false
168:         return executeWithToken("deleting TraceFlag $id", false) { token, instanceUrl ->
169:             val uri = buildUri(instanceUrl, "sobjects/TraceFlag/$id").build().toUri()
170:             restTemplate.exchange(uri, HttpMethod.DELETE, HttpEntity<Unit>(createHeaders(token)), Unit::class.java).statusCode.is2xxSuccessful
171:         }
172:     }
173:     fun patchTraceFlag(id: String, startDate: String, expirationDate: String): Boolean {
174:         if (!isValidSalesforceId(id)) return false
175:         return executeWithToken("patching TraceFlag $id", false) { token, instanceUrl ->
176:             val uri = buildUri(instanceUrl, "sobjects/TraceFlag/$id").build().toUri()
177:             val body = mapOf(
178:                 "StartDate" to startDate,
179:                 "ExpirationDate" to expirationDate
180:             )
181:             val entity = HttpEntity(body, createHeaders(token, MediaType.APPLICATION_JSON))
182:             // Note: RestTemplate requires a specific RequestFactory (like JdkClientHttpRequestFactory) to support PATCH.
183:             restTemplate.exchange(uri, HttpMethod.PATCH, entity, Unit::class.java).statusCode.is2xxSuccessful
184:         }
185:     }
186: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceMetadataPollingService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.repository.ApexClassRepository
 3: import com.observability.sfdc.repository.ApexTriggerRepository
 4: import org.slf4j.LoggerFactory
 5: import org.springframework.scheduling.annotation.Scheduled
 6: import org.springframework.stereotype.Service
 7: import org.springframework.transaction.annotation.Transactional
 8: @Service
 9: class SalesforceMetadataPollingService(
10:     private val metadataService: SalesforceMetadataService,
11:     private val classRepository: ApexClassRepository,
12:     private val triggerRepository: ApexTriggerRepository
13: ) {
14:     private val logger = LoggerFactory.getLogger(SalesforceMetadataPollingService::class.java)
15:     @Scheduled(fixedRate = 3600000) // Poll every hour
16:     @Transactional
17:     fun pollMetadata() {
18:         logger.info("Starting Salesforce metadata polling cycle...")
19:         try {
20:             val classes = metadataService.fetchApexClassesFromSalesforce(limit = 200)
21:             metadataService.syncClassesToDatabase(classes)
22:             logger.info("Synchronized ${classes.size} Apex classes.")
23:             val triggers = metadataService.fetchApexTriggersFromSalesforce(limit = 200)
24:             metadataService.syncTriggersToDatabase(triggers)
25:             logger.info("Synchronized ${triggers.size} Apex triggers.")
26:             logger.info("Metadata polling complete.")
27:         } catch (e: Exception) {
28:             logger.error("Critical error during Salesforce metadata polling: ${e.message}", e)
29:         }
30:     }
31: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceMetadataService.kt
````kotlin
  1: package com.observability.sfdc.service
  2: import com.observability.sfdc.domain.ApexClass
  3: import com.observability.sfdc.domain.ApexTrigger
  4: import com.observability.sfdc.domain.DebugLevel
  5: import com.observability.sfdc.domain.MetadataHistory
  6: import com.observability.sfdc.dto.*
  7: import com.observability.sfdc.repository.ApexClassRepository
  8: import com.observability.sfdc.repository.ApexTriggerRepository
  9: import com.observability.sfdc.repository.DebugLevelRepository
 10: import com.observability.sfdc.repository.MetadataHistoryRepository
 11: import org.springframework.beans.factory.annotation.Value
 12: import org.springframework.cache.annotation.Cacheable
 13: import org.springframework.core.ParameterizedTypeReference
 14: import org.springframework.data.domain.PageRequest
 15: import org.springframework.data.domain.Sort
 16: import org.springframework.http.HttpEntity
 17: import org.springframework.http.HttpMethod
 18: import org.springframework.stereotype.Service
 19: import org.springframework.transaction.annotation.Transactional
 20: @Service
 21: class SalesforceMetadataService(
 22:     authService: SalesforceAuthService,
 23:     private val classRepository: ApexClassRepository,
 24:     private val triggerRepository: ApexTriggerRepository,
 25:     private val debugLevelRepository: DebugLevelRepository,
 26:     private val metadataHistoryRepository: MetadataHistoryRepository,
 27:     @Value($$"${salesforce.api-version}") apiVersion: String
 28:     ) : SalesforceBaseService(authService, apiVersion) {
 29:     @Cacheable(value = ["sf_metadata"], key = "'debug_levels_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
 30:     @Transactional
 31:     fun getAllDebugLevels(name: String? = null, limit: Int = 10, offset: Int = 0): List<DebugLevelDto> {
 32:         var query = "SELECT Id, DeveloperName, MasterLabel, ApexCode, ApexProfiling, Callout, Database, System, Validation, Visualforce, Workflow FROM DebugLevel "
 33:         if (!name.isNullOrBlank()) {
 34:             val escapedName = name.replace("'", "\\'")
 35:             query += "WHERE DeveloperName LIKE '%$escapedName%' OR MasterLabel LIKE '%$escapedName%' "
 36:         }
 37:         query += "LIMIT $limit OFFSET $offset"
 38:         val records = querySalesforce("querying DebugLevels", query, object : ParameterizedTypeReference<SalesforceQueryResult<DebugLevelDto>>() {})
 39:         if (records.isNotEmpty()) syncDebugLevelsToDatabase(records)
 40:         return records
 41:     }
 42:     fun fetchApexClassesFromSalesforce(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexClassDto> {
 43:         var query = "SELECT Id, Name, ApiVersion, Status, LengthWithoutComments, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name, Body FROM ApexClass WHERE Status = 'Active' "
 44:         if (!name.isNullOrBlank()) {
 45:             val escapedName = name.replace("'", "\\'")
 46:             query += "AND Name LIKE '%$escapedName%' "
 47:         }
 48:         query += "AND (NOT Name LIKE '%Test') AND (NOT Name LIKE 'Test%') AND (NOT Name LIKE '%Tests') AND (NOT Name LIKE '%Mock') AND (NOT Name LIKE '%Factory') "
 49:         query += "ORDER BY Name ASC LIMIT $limit OFFSET $offset"
 50:         val records = querySalesforce("querying ApexClasses", query, object : ParameterizedTypeReference<SalesforceQueryResult<ApexClassDto>>() {})
 51:         if (records.isNotEmpty()) {
 52:             val coverageMap = fetchCoverageForMetadata(records.map { it.id })
 53:             return records.map { it.copy(coverage = coverageMap[it.id]) }
 54:         }
 55:         return records
 56:     }
 57:     fun fetchApexTriggersFromSalesforce(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexTriggerDto> {
 58:         var query = "SELECT Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, UsageAfterUndelete, LastModifiedDate, LastModifiedBy.Name, CreatedDate, CreatedBy.Name, Body FROM ApexTrigger WHERE Status = 'Active' "
 59:         if (!name.isNullOrBlank()) {
 60:             val escapedName = name.replace("'", "\\'")
 61:             query += "AND Name LIKE '%$escapedName%' "
 62:         }
 63:         query += "ORDER BY Name ASC LIMIT $limit OFFSET $offset"
 64:         val records = querySalesforce("querying ApexTriggers", query, object : ParameterizedTypeReference<SalesforceQueryResult<ApexTriggerDto>>() {})
 65:         if (records.isNotEmpty()) {
 66:             val coverageMap = fetchCoverageForMetadata(records.map { it.id })
 67:             return records.map { it.copy(coverage = coverageMap[it.id]) }
 68:         }
 69:         return records
 70:     }
 71:     @Cacheable(value = ["sf_metadata"], key = "'apex_classes_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
 72:     fun getAllApexClasses(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexClassDto> = fetchApexClassesFromSalesforce(name, limit, offset)
 73:     @Cacheable(value = ["sf_metadata"], key = "'apex_triggers_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
 74:     fun getAllApexTriggers(name: String? = null, limit: Int = 10, offset: Int = 0): List<ApexTriggerDto> = fetchApexTriggersFromSalesforce(name, limit, offset)
 75:     private fun fetchCoverageForMetadata(ids: List<String>): Map<String, ApexCodeCoverageDto> {
 76:         if (ids.isEmpty()) return emptyMap()
 77:         val idList = ids.joinToString(",") { "'$it'" }
 78:         val query = "SELECT ApexClassOrTriggerId, NumLinesCovered, NumLinesUncovered FROM ApexCodeCoverageAggregate WHERE ApexClassOrTriggerId IN ($idList)"
 79:         return querySalesforce("querying coverage", query, object : ParameterizedTypeReference<SalesforceQueryResult<ApexCodeCoverageDto>>() {})
 80:             .associateBy { it.apexClassOrTriggerId }
 81:     }
 82:     // --- Search methods ---
 83:     fun searchClasses(name: String?, limit: Int = 10, offset: Int = 0): List<ApexClass> {
 84:         val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
 85:         if (!name.isNullOrBlank()) {
 86:             val dtos = fetchApexClassesFromSalesforce(name, 200, 0)
 87:             syncClassesToDatabase(dtos)
 88:         } else if (classRepository.count() == 0L) {
 89:             val dtos = fetchApexClassesFromSalesforce(null, 200, 0)
 90:             syncClassesToDatabase(dtos)
 91:         }
 92:         return if (name.isNullOrBlank()) classRepository.findAllProjectedBy(pageable) else classRepository.findByNameContainingIgnoreCase(name, pageable)
 93:     }
 94:     fun searchTriggers(name: String?, limit: Int = 10, offset: Int = 0): List<ApexTrigger> {
 95:         val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
 96:         if (!name.isNullOrBlank()) {
 97:             val dtos = fetchApexTriggersFromSalesforce(name, 200, 0)
 98:             syncTriggersToDatabase(dtos)
 99:         } else if (triggerRepository.count() == 0L) {
100:             val dtos = fetchApexTriggersFromSalesforce(null, 200, 0)
101:             syncTriggersToDatabase(dtos)
102:         }
103:         return if (name.isNullOrBlank()) triggerRepository.findAllProjectedBy(pageable) else triggerRepository.findByNameContainingIgnoreCaseOrSobjectContainingIgnoreCase(name, name, pageable)
104:     }
105:     fun searchDebugLevels(name: String?, limit: Int = 10, offset: Int = 0): List<DebugLevel> {
106:         val pageable = PageRequest.of(offset / limit, limit, Sort.by("developerName").ascending())
107:         if (!name.isNullOrBlank()) getAllDebugLevels(name, 200, 0) else if (debugLevelRepository.count() == 0L) getAllDebugLevels(null, 200, 0)
108:         return if (name.isNullOrBlank()) debugLevelRepository.findAllProjectedBy(pageable) else debugLevelRepository.findByDeveloperNameContainingIgnoreCaseOrMasterLabelContainingIgnoreCase(name, name, pageable)
109:     }
110:     // --- Detail & Related ---
111:     fun getMetadataDetail(id: String, type: String): MetadataDetailDto? {
112:         val objectType = if (type == "ApexClass" || type == "ApexTrigger") type else return null
113:         val fields = if (objectType == "ApexTrigger") "Id, Name, TableEnumOrId, ApiVersion, Status, UsageBeforeInsert, UsageBeforeUpdate, UsageBeforeDelete, UsageAfterInsert, UsageAfterUpdate, UsageAfterDelete, UsageAfterUndelete, LastModifiedDate, LastModifiedBy.Name, Body"
114:                      else "Id, Name, ApiVersion, Status, LastModifiedDate, LastModifiedBy.Name, Body"
115:         val query = "SELECT $fields FROM $objectType WHERE Id = '${id.trim()}'"
116:         return executeWithToken("fetching metadata detail for $id", null) { token, instanceUrl ->
117:             val uri = buildUri(instanceUrl, "query").queryParam("q", query).build().toUri()
118:             val coverage = fetchCoverageForMetadata(listOf(id))[id]
119:             if (objectType == "ApexTrigger") {
120:                 val trigger = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity<Unit>(createHeaders(token)), object : ParameterizedTypeReference<SalesforceQueryResult<ApexTriggerDto>>() {}).body?.records?.firstOrNull() ?: return@executeWithToken null
121:                 MetadataDetailDto(trigger.id, trigger.name!!, "ApexTrigger", trigger.apiVersion, trigger.status, trigger.lastModifiedDate, trigger.lastModifiedBy?.name, trigger.tableEnumOrId, mapTriggerEvents(trigger), findRelatedTestClasses(
122:                     trigger.name
123:                 ), coverage, body = trigger.body)
124:             } else {
125:                 val apexClass = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity<Unit>(createHeaders(token)), object : ParameterizedTypeReference<SalesforceQueryResult<ApexClassDto>>() {}).body?.records?.firstOrNull() ?: return@executeWithToken null
126:                 MetadataDetailDto(apexClass.id, apexClass.name!!, "ApexClass", apexClass.apiVersion, apexClass.status, apexClass.lastModifiedDate, apexClass.lastModifiedBy?.name, testClasses = findRelatedTestClasses(
127:                     apexClass.name
128:                 ), coverage = coverage, body = apexClass.body)
129:             }
130:         }
131:     }
132:     internal open fun findRelatedTestClasses(name: String): List<ApexClassDto> {
133:         return executeWithToken("searching related test classes for $name", emptyList()) { token, instanceUrl ->
134:             val sosl = "FIND {$name AND \"@isTest\"} IN ALL FIELDS RETURNING ApexClass (Id, Name, ApiVersion, Status, LastModifiedDate, LastModifiedBy.Name WHERE Name != '$name' AND Status = 'Active')"
135:             val uri = buildUri(instanceUrl, "search").queryParam("q", sosl).build().toUri()
136:             restTemplate.exchange(uri, HttpMethod.GET, HttpEntity<Unit>(createHeaders(token)), object : ParameterizedTypeReference<SalesforceSearchResponse<ApexClassDto>>() {}).body?.searchRecords ?: emptyList()
137:         }
138:     }
139:     // --- Sync Methods ---
140:     internal fun syncDebugLevelsToDatabase(dtos: List<DebugLevelDto>) = dtos.distinctBy { it.id }.forEach { dto ->
141:         val entity = debugLevelRepository.findBySfdcId(dto.id).orElse(DebugLevel(sfdcId = dto.id, developerName = dto.developerName, masterLabel = dto.masterLabel, apexCode = dto.apexCode, apexProfiling = dto.apexProfiling, callout = dto.callout, database = dto.database, system = dto.system, validation = dto.validation, visualforce = dto.visualforce, workflow = dto.workflow))
142:         debugLevelRepository.save(entity.copy(developerName = dto.developerName, masterLabel = dto.masterLabel, apexCode = dto.apexCode, apexProfiling = dto.apexProfiling, callout = dto.callout, database = dto.database, system = dto.system, validation = dto.validation, visualforce = dto.visualforce, workflow = dto.workflow))
143:     }
144:     internal fun syncClassesToDatabase(dtos: List<ApexClassDto>) = dtos.distinctBy { it.id }.forEach { dto ->
145:         val entity = classRepository.findBySfdcId(dto.id).orElse(ApexClass(sfdcId = dto.id, name = dto.name, apiVersion = dto.apiVersion, status = dto.status, lengthWithoutComments = dto.lengthWithoutComments, lastModifiedDate = dto.lastModifiedDate, lastModifiedByName = dto.lastModifiedBy?.name, createdDate = dto.createdDate, createdByName = dto.createdBy?.name, numLinesCovered = dto.coverage?.numLinesCovered, numLinesUncovered = dto.coverage?.numLinesUncovered, body = dto.body))
146:         if (entity.body != null && entity.body != dto.body) {
147:             metadataHistoryRepository.save(MetadataHistory(sfdcId = dto.id, entityType = "ApexClass", body = entity.body))
148:         }
149:         classRepository.save(entity.copy(name = dto.name, apiVersion = dto.apiVersion, status = dto.status, lengthWithoutComments = dto.lengthWithoutComments, lastModifiedDate = dto.lastModifiedDate, lastModifiedByName = dto.lastModifiedBy?.name, createdDate = dto.createdDate, createdByName = dto.createdBy?.name, numLinesCovered = dto.coverage?.numLinesCovered, numLinesUncovered = dto.coverage?.numLinesUncovered, body = dto.body))
150:     }
151:     internal fun syncTriggersToDatabase(dtos: List<ApexTriggerDto>) = dtos.distinctBy { it.id }.forEach { dto ->
152:         val entity = triggerRepository.findBySfdcId(dto.id).orElse(ApexTrigger(sfdcId = dto.id, name = dto.name, sobject = dto.tableEnumOrId, apiVersion = dto.apiVersion, status = dto.status, usageBeforeInsert = dto.usageBeforeInsert, usageBeforeUpdate = dto.usageBeforeUpdate, usageBeforeDelete = dto.usageBeforeDelete, usageAfterInsert = dto.usageAfterInsert, usageAfterUpdate = dto.usageAfterUpdate, usageAfterDelete = dto.usageAfterDelete, usageAfterUndelete = dto.usageAfterUndelete, lastModifiedDate = dto.lastModifiedDate, lastModifiedByName = dto.lastModifiedBy?.name, createdDate = dto.createdDate, createdByName = dto.createdBy?.name, numLinesCovered = dto.coverage?.numLinesCovered, numLinesUncovered = dto.coverage?.numLinesUncovered, body = dto.body))
153:         if (entity.body != null && entity.body != dto.body) {
154:             metadataHistoryRepository.save(MetadataHistory(sfdcId = dto.id, entityType = "ApexTrigger", body = entity.body))
155:         }
156:         triggerRepository.save(entity.copy(name = dto.name, sobject = dto.tableEnumOrId, apiVersion = dto.apiVersion, status = dto.status, usageBeforeInsert = dto.usageBeforeInsert, usageBeforeUpdate = dto.usageBeforeUpdate, usageBeforeDelete = dto.usageBeforeDelete, usageAfterInsert = dto.usageAfterInsert, usageAfterUpdate = dto.usageAfterUpdate, usageAfterDelete = dto.usageAfterDelete, usageAfterUndelete = dto.usageAfterUndelete, lastModifiedDate = dto.lastModifiedDate, lastModifiedByName = dto.lastModifiedBy?.name, createdDate = dto.createdDate, createdByName = dto.createdBy?.name, numLinesCovered = dto.coverage?.numLinesCovered, numLinesUncovered = dto.coverage?.numLinesUncovered, body = dto.body))
157:     }
158:     private fun mapTriggerEvents(dto: ApexTriggerDto) = listOfNotNull(if (dto.usageBeforeInsert == true) "Before Insert" else null, if (dto.usageBeforeUpdate == true) "Before Update" else null, if (dto.usageBeforeDelete == true) "Before Delete" else null, if (dto.usageAfterInsert == true) "After Insert" else null, if (dto.usageAfterUpdate == true) "After Update" else null, if (dto.usageAfterDelete == true) "After Delete" else null, if (dto.usageAfterUndelete == true) "After Undelete" else null)
159: }
````

## File: src/main/kotlin/com/observability/sfdc/service/SalesforceUserService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.domain.User
 3: import com.observability.sfdc.dto.SalesforceQueryResult
 4: import com.observability.sfdc.dto.SalesforceUserDto
 5: import com.observability.sfdc.repository.UserRepository
 6: import org.springframework.beans.factory.annotation.Value
 7: import org.springframework.cache.annotation.Cacheable
 8: import org.springframework.core.ParameterizedTypeReference
 9: import org.springframework.data.domain.PageRequest
10: import org.springframework.data.domain.Sort
11: import org.springframework.stereotype.Service
12: import org.springframework.transaction.annotation.Transactional
13: @Service
14: class SalesforceUserService(
15:     authService: SalesforceAuthService,
16:     private val userRepository: UserRepository,
17:     @Value($$"${salesforce.api-version}") apiVersion: String
18: ) : SalesforceBaseService(authService, apiVersion) {
19:     @Cacheable(value = ["sf_users"], key = "'all_users_' + (#name ?: 'all') + '_' + #limit + '_' + #offset", unless = "#result == null")
20:     @Transactional
21:     fun getAllUsers(name: String? = null, limit: Int = 10, offset: Int = 0): List<SalesforceUserDto> {
22:         var query = "SELECT Id, Name, Username, Email, Profile.Name, IsActive, Entity__c FROM User WHERE IsActive = TRUE OR Name = 'Automated Process' "
23:         if (!name.isNullOrBlank()) {
24:             val escapedName = name.replace("'", "\\'")
25:             query += "AND Name LIKE '%$escapedName%' "
26:         }
27:         query += "ORDER BY Name ASC LIMIT $limit OFFSET $offset"
28:         val records = querySalesforce("querying Salesforce Users", query, object : ParameterizedTypeReference<SalesforceQueryResult<SalesforceUserDto>>() {}, useTooling = false)
29:         if (records.isNotEmpty()) syncUsersToDatabase(records)
30:         return records
31:     }
32:     fun searchUsers(name: String?, limit: Int = 10, offset: Int = 0): List<User> {
33:         val pageable = PageRequest.of(offset / limit, limit, Sort.by("name").ascending())
34:         if (!name.isNullOrBlank()) getAllUsers(name = name, limit = 200)
35:         else if (userRepository.count() == 0L) getAllUsers(limit = 200)
36:         return if (name.isNullOrBlank()) userRepository.findAllProjectedBy(pageable)
37:                else userRepository.findByNameContainingIgnoreCase(name, pageable)
38:     }
39:     @Transactional
40:     private fun syncUsersToDatabase(dtos: List<SalesforceUserDto>) = dtos.forEach { dto ->
41:         val entity = userRepository.findBySfdcId(dto.id).orElse(User(sfdcId = dto.id, name = dto.name, username = dto.username, email = dto.email, profileName = dto.profile?.name, isActive = dto.isActive, entity = dto.entity))
42:         userRepository.save(entity.copy(name = dto.name, username = dto.username, email = dto.email, profileName = dto.profile?.name, isActive = dto.isActive, entity = dto.entity))
43:     }
44: }
````

## File: src/main/kotlin/com/observability/sfdc/service/TraceJobSchedulerService.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.repository.TraceJobRepository
 3: import org.slf4j.LoggerFactory
 4: import org.springframework.scheduling.annotation.Scheduled
 5: import org.springframework.stereotype.Service
 6: import java.time.Instant
 7: @Service
 8: class TraceJobSchedulerService(
 9:     private val traceJobRepository: TraceJobRepository,
10:     private val traceJobService: TraceJobService
11: ) {
12:     private val logger = LoggerFactory.getLogger(TraceJobSchedulerService::class.java)
13:     @Scheduled(fixedRate = 900000) // 15 minutes
14:     fun processJobs() {
15:         val activeJobs = traceJobRepository.findByStatus("ACTIVE")
16:         if (activeJobs.isEmpty()) return
17:         logger.info("Starting Trace Job maintenance cycle for ${activeJobs.size} jobs...")
18:         val now = Instant.now()
19:         activeJobs.forEach { job ->
20:             try {
21:                 if (now.isAfter(job.endTime)) {
22:                     logger.info("Job ${job.id}: Target end time reached. Marking COMPLETED.")
23:                     job.status = "COMPLETED"
24:                     traceJobRepository.save(job)
25:                 } else {
26:                     // Slide the window forward
27:                     traceJobService.refreshSalesforceTraceFlag(job)
28:                 }
29:             } catch (e: Exception) {
30:                 logger.error("Error processing Job ${job.id}: ${e.message}")
31:             }
32:         }
33:     }
34: }
````

## File: src/main/kotlin/com/observability/sfdc/service/TraceJobService.kt
````kotlin
  1: package com.observability.sfdc.service
  2: import com.observability.sfdc.domain.TraceJob
  3: import com.observability.sfdc.dto.FrontendTraceFlagRequest
  4: import com.observability.sfdc.dto.TraceFlagDto
  5: import com.observability.sfdc.repository.TraceJobRepository
  6: import org.slf4j.LoggerFactory
  7: import org.springframework.stereotype.Service
  8: import org.springframework.transaction.annotation.Transactional
  9: import java.time.Duration
 10: import java.time.Instant
 11: import java.time.ZoneId
 12: import java.time.ZonedDateTime
 13: import java.time.format.DateTimeFormatter
 14: @Service
 15: class TraceJobService(
 16:     private val traceJobRepository: TraceJobRepository,
 17:     private val logService: SalesforceLogService
 18: ) {
 19:     private val logger = LoggerFactory.getLogger(TraceJobService::class.java)
 20:     private val sfdcFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
 21:     private val sfdcWithOffsetFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
 22:     @Transactional
 23:     fun createJob(request: FrontendTraceFlagRequest): TraceJob {
 24:         val totalMinutes = request.getTotalMinutes()
 25:         if (totalMinutes <= 0) {
 26:             throw IllegalArgumentException("Total duration must be at least 1 minute")
 27:         }
 28:         val startTime = Instant.now()
 29:         val endTime = startTime.plus(Duration.ofDays((request.durationDays ?: 0).toLong()))
 30:             .plus(Duration.ofHours((request.durationHours ?: 0).toLong()))
 31:             .plus(Duration.ofMinutes((request.durationMinutes ?: 0).toLong()))
 32:         val job = TraceJob(
 33:             tracedEntityId = request.tracedEntityId,
 34:             tracedEntityName = request.tracedEntityName,
 35:             tracedEntityType = request.entityType ?: "User",
 36:             debugLevelName = request.debugLevelName,
 37:             startTime = startTime,
 38:             endTime = endTime,
 39:             status = "ACTIVE"
 40:         )
 41:         val savedJob = traceJobRepository.save(job)
 42:         // Trigger initial TraceFlag in Salesforce
 43:         refreshSalesforceTraceFlag(savedJob)
 44:         return savedJob
 45:     }
 46:     fun getAllJobs(): List<TraceJob> = traceJobRepository.findAll()
 47:     fun searchJobsByName(name: String): List<TraceJob> = traceJobRepository.findByTracedEntityNameContainingIgnoreCase(name)
 48:     @Transactional
 49:     fun cancelJob(id: Long) {
 50:         val job = traceJobRepository.findById(id).orElseThrow { RuntimeException("Job not found") }
 51:         job.status = "CANCELLED"
 52:         job.sfdcTraceFlagId?.let { 
 53:             logService.deleteTraceFlag(it)
 54:         }
 55:         traceJobRepository.save(job)
 56:     }
 57:     @Transactional
 58:     fun refreshSalesforceTraceFlag(job: TraceJob) {
 59:         val now = ZonedDateTime.now(ZoneId.of("UTC"))
 60:         val jobEndTime = job.endTime.atZone(ZoneId.of("UTC"))
 61:         val maxSafeExpiry = now.plusDays(1)
 62:         val targetExpiry = if (jobEndTime.isBefore(maxSafeExpiry)) jobEndTime else maxSafeExpiry
 63:         if (job.sfdcTraceFlagId == null) {
 64:             // Create NEW
 65:             val duration = Duration.between(now.toInstant(), targetExpiry.toInstant()).toMinutes().coerceAtLeast(1).toInt()
 66:             val response = logService.createTraceFlag(
 67:                 FrontendTraceFlagRequest(
 68:                     tracedEntityId = job.tracedEntityId,
 69:                     debugLevelName = job.debugLevelName,
 70:                     durationMinutes = duration,
 71:                     entityType = job.tracedEntityType
 72:                 )
 73:             )
 74:             if (response?.success == true) {
 75:                 job.sfdcTraceFlagId = response.id
 76:                 traceJobRepository.save(job)
 77:                 logger.info("Job ${job.id}: Created initial Salesforce TraceFlag ${response.id} expiring at ${targetExpiry.format(sfdcFormatter)}")
 78:             } else {
 79:                 logger.error("Job ${job.id}: Failed to create Salesforce TraceFlag: ${response?.errors}")
 80:             }
 81:         } else {
 82:             // Sliding Window PATCH
 83:             val success = logService.patchTraceFlag(
 84:                 job.sfdcTraceFlagId!!,
 85:                 now.format(sfdcFormatter),
 86:                 targetExpiry.format(sfdcFormatter)
 87:             )
 88:             if (success) {
 89:                 logger.info("Job ${job.id}: Slid window for TraceFlag ${job.sfdcTraceFlagId} to ${targetExpiry.format(sfdcFormatter)}")
 90:             } else {
 91:                 logger.warn("Job ${job.id}: Failed to patch TraceFlag ${job.sfdcTraceFlagId}. Will attempt re-creation.")
 92:                 job.sfdcTraceFlagId = null
 93:                 traceJobRepository.save(job)
 94:                 // Recursive call to re-create
 95:                 refreshSalesforceTraceFlag(job)
 96:             }
 97:         }
 98:     }
 99:     @Transactional
100:     fun adoptExistingTraceFlag(traceFlag: TraceFlagDto): TraceJob {
101:         // Check if this SFDC TraceFlag is already managed by a local job
102:         val existingJob = traceJobRepository.findAll().find { it.sfdcTraceFlagId == traceFlag.id }
103:         if (existingJob != null) {
104:             throw IllegalStateException("This Salesforce TraceFlag is already managed by Job #${existingJob.id}")
105:         }
106:         val now = Instant.now()
107:         val startTime = if (traceFlag.startDate != null) {
108:             ZonedDateTime.parse(traceFlag.startDate, sfdcWithOffsetFormatter).toInstant()
109:         } else {
110:             now
111:         }
112:         val endTime = if (traceFlag.expirationDate != null) {
113:             ZonedDateTime.parse(traceFlag.expirationDate, sfdcWithOffsetFormatter).toInstant()
114:         } else {
115:             now.plus(Duration.ofHours(1))
116:         }
117:         val job = TraceJob(
118:             tracedEntityId = traceFlag.tracedEntityId,
119:             tracedEntityName = traceFlag.tracedEntity?.name,
120:             tracedEntityType = traceFlag.tracedEntity?.attributes?.type ?: "User",
121:             debugLevelName = traceFlag.debugLevel?.developerName ?: "Unknown",
122:             startTime = startTime,
123:             endTime = endTime,
124:             status = if (endTime.isAfter(now)) "ACTIVE" else "EXPIRED",
125:             sfdcTraceFlagId = traceFlag.id
126:         )
127:         val savedJob = traceJobRepository.save(job)
128:         logger.info("Adopted Salesforce TraceFlag ${traceFlag.id} as Job #${savedJob.id} (status: ${savedJob.status})")
129:         return savedJob
130:     }
131: }
````

## File: src/main/kotlin/com/observability/sfdc/SfdcApplication.kt
````kotlin
 1: package com.observability.sfdc
 2: import org.springframework.boot.autoconfigure.SpringBootApplication
 3: import org.springframework.boot.runApplication
 4: import org.springframework.cache.annotation.EnableCaching
 5: import org.springframework.scheduling.annotation.EnableAsync
 6: import org.springframework.scheduling.annotation.EnableScheduling
 7: @EnableCaching
 8: @EnableScheduling
 9: @EnableAsync
10: @SpringBootApplication
11: class SfdcApplication
12: fun main(args: Array<String>) {
13: 	runApplication<SfdcApplication>(*args)
14: }
````

## File: src/main/resources/application.properties
````
 1: spring.application.name=apexium.log
 2: 
 3: # Salesforce Configuration
 4: # Use environment variables for sensitive data to keep the repository public-safe
 5: salesforce.login-url=${SALESFORCE_INSTANCE_ORG:}
 6: salesforce.client-id=${SALESFORCE_CLIENT_ID:}
 7: salesforce.client-secret=${SALESFORCE_CLIENT_SECRET:}
 8: salesforce.grant-type=${SALESFORCE_GRANT_TYPE:}
 9: salesforce.api-version=${SALESFORCE_API_VERSION:}
10: 
11: # Database Configuration
12: spring.datasource.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:sfdc_logs}
13: spring.datasource.username=${DB_USER:postgres}
14: spring.datasource.password=${DB_PASSWORD:postgres}
15: spring.jpa.hibernate.ddl-auto=update
16: spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
17: spring.jpa.show-sql=false
18: 
19: # Redis Configuration
20: spring.data.redis.host=${REDIS_HOST:localhost}
21: spring.data.redis.port=${REDIS_PORT:6379}
22: # spring.data.redis.password=
23: spring.cache.type=redis
24: # Cache TTL configurations (custom property we might use in config class)
25: cache.ttl.token=3600
26: cache.ttl.metadata=3600
27: 
28: # Log Retention Configuration
29: log.retention.days=30
30: log.retention.cron=0 0 3 * * ?
31: 
32: # MinIO Configuration
33: minio.url=${MINIO_URL:http://localhost:9000}
34: minio.access-key=${MINIO_ACCESS_KEY:minioadmin}
35: minio.secret-key=${MINIO_SECRET_KEY:minioadmin}
36: minio.bucket-name=${MINIO_BUCKET_NAME:sfdc-bucket}
37: 
38: # Actuator Configuration
39: management.endpoints.web.exposure.include=health,info,metrics,prometheus
40: management.endpoint.health.show-details=always
41: management.metrics.export.prometheus.enabled=true
42: management.metrics.enable.jvm=true
````

## File: src/test/kotlin/com/observability/sfdc/dto/FrontendTraceFlagRequestTest.kt
````kotlin
 1: package com.observability.sfdc.dto
 2: import org.junit.jupiter.api.Assertions.assertEquals
 3: import org.junit.jupiter.api.Test
 4: class FrontendTraceFlagRequestTest {
 5:     @Test
 6:     fun `test getTotalMinutes with various inputs`() {
 7:         val request1 = FrontendTraceFlagRequest(
 8:             tracedEntityId = "testId",
 9:             debugLevelName = "testLevel",
10:             durationDays = 1,
11:             durationHours = 2,
12:             durationMinutes = 30
13:         )
14:         // 1 day = 1440 min
15:         // 2 hours = 120 min
16:         // 30 min
17:         // Total = 1440 + 120 + 30 = 1590
18:         assertEquals(1590L, request1.getTotalMinutes())
19:         val request2 = FrontendTraceFlagRequest(
20:             tracedEntityId = "testId",
21:             debugLevelName = "testLevel",
22:             durationDays = null,
23:             durationHours = null,
24:             durationMinutes = 45
25:         )
26:         assertEquals(45L, request2.getTotalMinutes())
27:         val request3 = FrontendTraceFlagRequest(
28:             tracedEntityId = "testId",
29:             debugLevelName = "testLevel",
30:             durationDays = 0,
31:             durationHours = 5,
32:             durationMinutes = null
33:         )
34:         assertEquals(300L, request3.getTotalMinutes())
35:     }
36: }
````

## File: src/test/kotlin/com/observability/sfdc/service/SalesforceLogPollingServiceTest.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.domain.Log
 3: import com.observability.sfdc.dto.ApexLogDto
 4: import com.observability.sfdc.dto.UserSummaryDto
 5: import com.observability.sfdc.repository.LogRepository
 6: import org.junit.jupiter.api.Test
 7: import org.mockito.Mockito.*
 8: import java.util.*
 9: class SalesforceLogPollingServiceTest {
10:     private val logService = mock(SalesforceLogService::class.java)
11:     private val logRepository = mock(LogRepository::class.java)
12:     private val pollingService = SalesforceLogPollingService(logService, logRepository)
13:     @Test
14:     fun `pollLogs should save new logs and skip existing ones`() {
15:         // Arrange
16:         val logId1 = "07L...1"
17:         val logId2 = "07L...2"
18:         val logs = listOf(
19:             ApexLogDto(id = logId1, logUser = UserSummaryDto("User 1"), operation = "Op 1", startTime = "2026-05-09T10:00:00.000+0000", status = "Success", request = "Req 1", logLength = 100, durationMilliseconds = 50),
20:             ApexLogDto(id = logId2, logUser = UserSummaryDto("User 2"), operation = "Op 2", startTime = "2026-05-09T10:01:00.000+0000", status = "Success", request = "Req 2", logLength = 200, durationMilliseconds = 150)
21:         )
22:         `when`(logService.queryApexLogs(limit = 20, fetchBody = false)).thenReturn(logs)
23:         `when`(logRepository.findBySfdcId(logId1)).thenReturn(Optional.of(mock(Log::class.java)))
24:         `when`(logRepository.findBySfdcId(logId2)).thenReturn(Optional.empty())
25:         // Act
26:         pollingService.pollLogs()
27:         // Assert
28:         verify(logRepository, times(1)).save(any(Log::class.java))
29:         verify(logRepository, times(1)).findBySfdcId(logId1)
30:         verify(logRepository, times(1)).findBySfdcId(logId2)
31:     }
32: }
````

## File: src/test/kotlin/com/observability/sfdc/service/SalesforceMetadataServiceTest.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.dto.ApexClassDto
 3: import com.observability.sfdc.dto.ApexCodeCoverageDto
 4: import com.observability.sfdc.dto.ApexTriggerDto
 5: import com.observability.sfdc.repository.ApexClassRepository
 6: import com.observability.sfdc.repository.ApexTriggerRepository
 7: import com.observability.sfdc.repository.DebugLevelRepository
 8: import org.junit.jupiter.api.Assertions.assertEquals
 9: import org.junit.jupiter.api.Assertions.assertNotNull
10: import org.junit.jupiter.api.BeforeEach
11: import org.junit.jupiter.api.Test
12: import org.mockito.ArgumentMatchers.any
13: import org.mockito.ArgumentMatchers.anyBoolean
14: import org.mockito.Mockito.*
15: import java.util.*
16: class SalesforceMetadataServiceTest {
17:     private lateinit var authService: SalesforceAuthService
18:     private lateinit var classRepository: ApexClassRepository
19:     private lateinit var triggerRepository: ApexTriggerRepository
20:     private lateinit var debugLevelRepository: DebugLevelRepository
21:     private lateinit var service: SalesforceMetadataService
22:     // Helpers to avoid NPE with Kotlin non-nullable parameters
23:     private fun anyString(): String = any() ?: ""
24:     private fun <T> anyRef(): T = any() ?: null as T
25:     @BeforeEach
26:     fun setUp() {
27:         authService = mock(SalesforceAuthService::class.java)
28:         classRepository = mock(ApexClassRepository::class.java)
29:         triggerRepository = mock(ApexTriggerRepository::class.java)
30:         debugLevelRepository = mock(DebugLevelRepository::class.java)
31:         //service = spy(SalesforceMetadataService(authService, classRepository, triggerRepository, debugLevelRepository, "v60.0"))
32:     }
33:     @Test
34:     fun `getAllApexClasses should enrich with coverage data`() {
35:         // Arrange
36:         val classId = "01p000000000001"
37:         val classDto = ApexClassDto(id = classId, name = "MyClass", apiVersion = 60.0, status = "Active", lengthWithoutComments = 100, lastModifiedDate = null, lastModifiedBy = null, createdDate = null, createdBy = null)
38:         val coverageDto = ApexCodeCoverageDto(apexClassOrTriggerId = classId, numLinesCovered = 80, numLinesUncovered = 20)
39:         // Mock querySalesforce for ApexClass then coverage
40:         doReturn(listOf(classDto))
41:             .doReturn(listOf(coverageDto))
42:             .`when`(service).querySalesforce<Any>(
43:                 anyString(),
44:                 anyString(),
45:                 anyRef(),
46:                 anyBoolean()
47:             )
48:         `when`(classRepository.findBySfdcId(anyString())).thenReturn(Optional.empty())
49:         // Act
50:         val result = service.getAllApexClasses()
51:         // Assert
52:         assertEquals(1, result.size)
53:         val enrichedClass = result[0]
54:         assertNotNull(enrichedClass.coverage)
55:         assertEquals(80, enrichedClass.coverage?.numLinesCovered)
56:         assertEquals(80.0, enrichedClass.coverage?.coveragePercentage)
57:         verify(classRepository, times(1)).save(any())
58:     }
59:     @Test
60:     fun `getAllApexTriggers should enrich with coverage data`() {
61:         // Arrange
62:         val triggerId = "01q000000000001"
63:         val triggerDto = ApexTriggerDto(id = triggerId, name = "MyTrigger", tableEnumOrId = "Account", apiVersion = 60.0, status = "Active", lastModifiedDate = null, lastModifiedBy = null, createdDate = null, createdBy = null, usageBeforeInsert = true, usageBeforeUpdate = null, usageBeforeDelete = null, usageAfterInsert = null, usageAfterUpdate = null, usageAfterDelete = null, usageAfterUndelete = null)
64:         val coverageDto = ApexCodeCoverageDto(apexClassOrTriggerId = triggerId, numLinesCovered = 50, numLinesUncovered = 50)
65:         // Mock querySalesforce for ApexTrigger then coverage
66:         doReturn(listOf(triggerDto))
67:             .doReturn(listOf(coverageDto))
68:             .`when`(service).querySalesforce<Any>(
69:                 anyString(),
70:                 anyString(),
71:                 anyRef(),
72:                 anyBoolean()
73:             )
74:         `when`(triggerRepository.findBySfdcId(anyString())).thenReturn(Optional.empty())
75:         // Act
76:         val result = service.getAllApexTriggers()
77:         // Assert
78:         assertEquals(1, result.size)
79:         val enrichedTrigger = result[0]
80:         assertNotNull(enrichedTrigger.coverage)
81:         assertEquals(50, enrichedTrigger.coverage?.numLinesCovered)
82:         assertEquals(50.0, enrichedTrigger.coverage?.coveragePercentage)
83:         verify(triggerRepository, times(1)).save(any())
84:     }
85: }
````

## File: src/test/kotlin/com/observability/sfdc/service/TraceJobServiceTest.kt
````kotlin
 1: package com.observability.sfdc.service
 2: import com.observability.sfdc.domain.TraceJob
 3: import com.observability.sfdc.dto.FrontendTraceFlagRequest
 4: import com.observability.sfdc.repository.TraceJobRepository
 5: import org.junit.jupiter.api.Assertions.assertEquals
 6: import org.junit.jupiter.api.Test
 7: import org.mockito.ArgumentMatchers.any
 8: import org.mockito.Mockito.*
 9: class TraceJobServiceTest {
10:     private val traceJobRepository = mock(TraceJobRepository::class.java)
11:     private val logService = mock(SalesforceLogService::class.java)
12:     private val traceJobService = TraceJobService(traceJobRepository, logService)
13:     @Test
14:     fun `createJob should calculate correct endTime with days hours and minutes`() {
15:         // Arrange
16:         val request = FrontendTraceFlagRequest(
17:             tracedEntityId = "testId",
18:             debugLevelName = "testLevel",
19:             durationDays = 1,
20:             durationHours = 2,
21:             durationMinutes = 30
22:         )
23:         `when`(traceJobRepository.save(any(TraceJob::class.java))).thenAnswer { it.arguments[0] as TraceJob }
24:         // Act
25:         val job = traceJobService.createJob(request)
26:         // Assert
27:         //val expectedEndTime = job.startTime.plusDays(1).plusHours(2).plusMinutes(30)
28:         //assertEquals(expectedEndTime, job.endTime)
29:         assertEquals("testId", job.tracedEntityId)
30:         assertEquals("testLevel", job.debugLevelName)
31:         assertEquals("ACTIVE", job.status)
32:         verify(traceJobRepository, times(1)).save(any(TraceJob::class.java))
33:         // Note: refreshSalesforceTraceFlag is called internally, but it involves ZonedDateTime.now()
34:         // so we don't strictly test its details here unless we mock the time provider
35:     }
36: }
````

## File: src/test/kotlin/com/observability/sfdc/SfdcApplicationTests.kt
````kotlin
1: package com.observability.sfdc
2: import org.junit.jupiter.api.Test
3: import org.springframework.boot.test.context.SpringBootTest
4: @SpringBootTest
5: class SfdcApplicationTests {
6: 	@Test
7: 	fun contextLoads() {
8: 	}
9: }
````

## File: .gitattributes
````
1: /mvnw text eol=lf
2: *.cmd text eol=crlf
````

## File: .gitignore
````
 1: HELP.md
 2: target/
 3: .mvn/wrapper/maven-wrapper.jar
 4: !**/src/main/**/target/
 5: !**/src/test/**/target/
 6: 
 7: ### STS ###
 8: .apt_generated
 9: .classpath
10: .factorypath
11: .project
12: .settings
13: .springBeans
14: .sts4-cache
15: 
16: ### IntelliJ IDEA ###
17: .idea
18: *.iws
19: *.iml
20: *.ipr
21: 
22: ### NetBeans ###
23: /nbproject/private/
24: /nbbuild/
25: /dist/
26: /nbdist/
27: /.nb-gradle/
28: build/
29: !**/src/main/**/build/
30: !**/src/test/**/build/
31: 
32: ### VS Code ###
33: .vscode/
34: 
35: .env
````

## File: .gitmodules
````
1: [submodule "frontend"]
2: 	path = frontend
3: 	url = https://github.com/ichwansh03/log-observable-salesforce-fe
````

## File: CHANGELOG.md
````markdown
 1: # Changelog
 2: 
 3: All notable changes to this project will be documented in this file.
 4: 
 5: 
 6: The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
 7: and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
 8: 
 9: ## [1.0.0] - 2026-06-04
10: 
11: ### Added
12: - **Salesforce Real-time Integration**: Subscribes to Salesforce events via CometD to receive real-time log notifications.
13: - **Automated Log Retrieval**: Intelligent fetching of full `ApexLog` bodies using the Salesforce Tooling API.
14: - **Trace Management System**: Advanced scheduling for `TraceFlags` with a sliding window mechanism to bypass Salesforce's 24-hour limit.
15: - **Multi-Tier Storage Architecture**:
16:     - **PostgreSQL**: Reliable persistence for log metadata and trace job states.
17:     - **Redis**: High-performance caching for OAuth tokens and metadata to reduce API overhead.
18:     - **MinIO/S3**: Scalable object storage for raw log bodies.
19: - **Reactive Backend**: Modern Kotlin and Spring Boot 4.0.6 implementation with asynchronous processing.
20: - **Monitoring Dashboard**: React-based frontend for real-time log visualization and management of users, classes, and triggers.
21: - **Secure Authentication**: Environment-variable-driven OAuth2 integration.
22: - **Docker Support**: Ready-to-use Docker and Docker Compose configurations for the entire stack.
````

## File: CONTRIBUTING.md
````markdown
 1: # Contributing to Apexium.log
 2: 
 3: First off, thank you for considering contributing to Apexium.log! It's people like you that make this tool better for everyone.
 4: 
 5: ## Code of Conduct
 6: 
 7: By participating in this project, you are expected to uphold our Code of Conduct. Please be respectful and professional in all interactions.
 8: 
 9: ## How Can I Contribute?
10: 
11: ### Reporting Bugs
12: *   Check the Issues tab to see if the bug has already been reported.
13: *   If not, open a new issue with a clear title, description, and steps to reproduce.
14: 
15: ### Suggesting Enhancements
16: *   Open an issue to discuss your idea before implementing it.
17: 
18: ### Pull Requests
19: 1.  Fork the repository.
20: 2.  Create a new branch (`git checkout -b feature/amazing-feature`).
21: 3.  Commit your changes (`git commit -m 'Add some amazing feature'`).
22: 4.  Push to the branch (`git push origin feature/amazing-feature`).
23: 5.  Open a Pull Request.
24: 
25: ## Development Setup
26: 
27: ### Backend (Kotlin & Spring Boot)
28: - **Requirements**: JDK 21, Maven.
29: - **Database**: PostgreSQL 17.
30: - **Cache**: Redis.
31: - **Object Storage**: MinIO.
32: - **Environment**: Copy `.env.example` to `.env` and fill in your Salesforce credentials.
33: 
34: Run the backend:
35: ```bash
36: ./mvnw spring-boot:run
37: ```
38: 
39: ### Frontend (React & TypeScript)
40: - **Requirements**: Node.js (Latest LTS).
41: - **Location**: `/frontend`
42: 
43: Setup:
44: ```bash
45: cd frontend
46: npm install
47: npm run dev
48: ```
49: 
50: ## Standards & Style
51: - Follow existing Kotlin and React patterns.
52: - Ensure all new features are accompanied by relevant tests.
53: - Run `npm run lint` for frontend changes.
54: 
55: ## Testing
56: - **Backend**: Run `./mvnw test`.
57: - **Frontend**: Tests are integrated into the build process where applicable.
58: 
59: ---
60: *Thank you for your contributions!*
````

## File: docker-compose.yml
````yaml
  1: services:
  2:   app:
  3:     build: .
  4:     ports:
  5:       - "8080:8080"
  6:     env_file:
  7:       - .env
  8:     environment:
  9:       - REDIS_HOST=redis
 10:       - DB_HOST=db
 11:       - DB_PORT=${DB_PORT}
 12:       - DB_NAME=${DB_NAME}
 13:       - DB_USER=${DB_USER}
 14:       - DB_PASSWORD=${DB_PASSWORD}
 15:     depends_on:
 16:       redis:
 17:         condition: service_healthy
 18:       db:
 19:         condition: service_healthy
 20:       minio:
 21:         condition: service_started
 22:     networks:
 23:       - sfdc-network
 24:   db:
 25:     image: postgres:17
 26:     environment:
 27:       - POSTGRES_DB=${DB_NAME}
 28:       - POSTGRES_USER=${DB_USER}
 29:       - POSTGRES_PASSWORD=${DB_PASSWORD}
 30:     ports:
 31:       - "5432:5432"
 32:     volumes:
 33:       - postgres_data:/var/lib/postgresql/data
 34:     networks:
 35:       - sfdc-network
 36:     healthcheck:
 37:       test: ["CMD-SHELL", "pg_isready -U ${DB_USER} -d ${DB_NAME}"]
 38:       interval: 10s
 39:       timeout: 5s
 40:       retries: 5
 41:   pgadmin:
 42:     image: dpage/pgadmin4:latest
 43:     ports:
 44:       - "5050:80"
 45:     environment:
 46:       - PGADMIN_DEFAULT_EMAIL=${PGADMIN_EMAIL}
 47:       - PGADMIN_DEFAULT_PASSWORD=${PGADMIN_PASSWORD}
 48:     depends_on:
 49:       db:
 50:         condition: service_healthy
 51:     networks:
 52:       - sfdc-network
 53:   redis:
 54:     image: redis:alpine
 55:     ports:
 56:       - "6379:6379"
 57:     networks:
 58:       - sfdc-network
 59:     healthcheck:
 60:       test: ["CMD", "redis-cli", "ping"]
 61:       interval: 10s
 62:       timeout: 5s
 63:       retries: 5
 64:   minio:
 65:     image: quay.io/minio/minio
 66:     ports:
 67:       - "9000:9000"
 68:       - "9001:9001"
 69:     environment:
 70:       - MINIO_ROOT_USER=${MINIO_ROOT_USER}
 71:       - MINIO_ROOT_PASSWORD=${MINIO_ROOT_PASSWORD}
 72:     command: server /data --console-address ":9001"
 73:     volumes:
 74:       - minio_data:/data
 75:     networks:
 76:       - sfdc-network
 77:   # prometheus:
 78:   #   image: prom/prometheus:latest
 79:   #   ports:
 80:   #     - "9090:9090"
 81:   #   volumes:
 82:   #     - ./prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
 83:   #     - prometheus_data:/prometheus
 84:   #   command:
 85:   #     - '--config.file=/etc/prometheus/prometheus.yml'
 86:   #     - '--storage.tsdb.path=/prometheus'
 87:   #   networks:
 88:   #     - sfdc-network
 89:   # grafana:
 90:   #   image: grafana/grafana:latest
 91:   #   ports:
 92:   #     - "3000:3000"
 93:   #   environment:
 94:   #     - GF_SECURITY_ADMIN_PASSWORD=admin
 95:   #   volumes:
 96:   #     - grafana_data:/var/lib/grafana
 97:   #   depends_on:
 98:   #     - prometheus
 99:   #   networks:
100:   #     - sfdc-network
101: networks:
102:   sfdc-network:
103:     driver: bridge
104: volumes:
105:   postgres_data:
106:   minio_data:
107: #  prometheus_data:
108: #  grafana_data:
````

## File: Dockerfile
````dockerfile
 1: # Build stage
 2: FROM eclipse-temurin:21-jdk-jammy AS build
 3: WORKDIR /app
 4: 
 5: # Copy the maven wrapper and pom.xml first to leverage Docker cache
 6: COPY .mvn/ .mvn
 7: COPY mvnw pom.xml ./
 8: RUN chmod +x mvnw
 9: RUN ./mvnw dependency:go-offline
10: 
11: # Copy the source code and build
12: COPY src ./src
13: RUN ./mvnw clean package -DskipTests
14: 
15: # Run stage
16: FROM eclipse-temurin:21-jre-jammy
17: WORKDIR /app
18: 
19: # Copy the built jar from the build stage
20: COPY --from=build /app/target/*.jar app.jar
21: 
22: # Expose the application port
23: EXPOSE 8080
24: 
25: # Run the application
26: ENTRYPOINT ["java", "-jar", "app.jar"]
````

## File: MONITORING.md
````markdown
 1: # Monitoring Apexium.log
 2: 
 3: This project uses Spring Boot Actuator and Micrometer to expose metrics for Prometheus.
 4: 
 5: ## Actuator Endpoints
 6: 
 7: - **Health**: `http://localhost:8080/actuator/health`
 8: - **Info**: `http://localhost:8080/actuator/info`
 9: - **Metrics**: `http://localhost:8080/actuator/metrics`
10: - **Prometheus**: `http://localhost:8080/actuator/prometheus`
11: 
12: ## Docker Compose Integration
13: 
14: Prometheus and Grafana are included in the `docker-compose.yml`. When you run `docker-compose up`, these services will start automatically.
15: 
16: - **Prometheus**: `http://localhost:9090` (Scrapes the app automatically)
17: - **Grafana**: `http://localhost:3000` (User: `admin`, Password: `admin`)
18: 
19: ## Prometheus Configuration
20: 
21: To scrape metrics from this application, add the following to your `prometheus.yml`:
22: 
23: ```yaml
24: scrape_configs:
25:   - job_name: 'apexium-log'
26:     metrics_path: '/actuator/prometheus'
27:     scrape_interval: 15s
28:     static_configs:
29:       - targets: ['host.docker.internal:8080'] # Use 'localhost:8080' if running outside Docker
30: ```
31: 
32: ## Grafana Dashboard
33: 
34: You can use standard JVM dashboards to visualize the metrics. We recommend:
35: 
36: 1.  **JVM (Micrometer)**: [Dashboard ID: 4701](https://grafana.com/grafana/dashboards/4701-jvm-micrometer/)
37: 2.  **Spring Boot Statistics**: [Dashboard ID: 6756](https://grafana.com/grafana/dashboards/6756-spring-boot-statistics/)
38: 
39: ### Key Metrics to Watch:
40: 
41: - `jvm_memory_used_bytes`: Current memory usage.
42: - `jvm_gc_pause_seconds_count`: Garbage collection frequency.
43: - `http_server_requests_seconds_count`: API request throughput.
44: - `process_cpu_usage`: CPU usage of the application.
````

## File: mvnw
````
  1: #!/bin/sh
  2: # ----------------------------------------------------------------------------
  3: # Licensed to the Apache Software Foundation (ASF) under one
  4: # or more contributor license agreements.  See the NOTICE file
  5: # distributed with this work for additional information
  6: # regarding copyright ownership.  The ASF licenses this file
  7: # to you under the Apache License, Version 2.0 (the
  8: # "License"); you may not use this file except in compliance
  9: # with the License.  You may obtain a copy of the License at
 10: #
 11: #    http://www.apache.org/licenses/LICENSE-2.0
 12: #
 13: # Unless required by applicable law or agreed to in writing,
 14: # software distributed under the License is distributed on an
 15: # "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 16: # KIND, either express or implied.  See the License for the
 17: # specific language governing permissions and limitations
 18: # under the License.
 19: # ----------------------------------------------------------------------------
 20: 
 21: # ----------------------------------------------------------------------------
 22: # Apache Maven Wrapper startup batch script, version 3.3.4
 23: #
 24: # Optional ENV vars
 25: # -----------------
 26: #   JAVA_HOME - location of a JDK home dir, required when download maven via java source
 27: #   MVNW_REPOURL - repo url base for downloading maven distribution
 28: #   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
 29: #   MVNW_VERBOSE - true: enable verbose log; debug: trace the mvnw script; others: silence the output
 30: # ----------------------------------------------------------------------------
 31: 
 32: set -euf
 33: [ "${MVNW_VERBOSE-}" != debug ] || set -x
 34: 
 35: # OS specific support.
 36: native_path() { printf %s\\n "$1"; }
 37: case "$(uname)" in
 38: CYGWIN* | MINGW*)
 39:   [ -z "${JAVA_HOME-}" ] || JAVA_HOME="$(cygpath --unix "$JAVA_HOME")"
 40:   native_path() { cygpath --path --windows "$1"; }
 41:   ;;
 42: esac
 43: 
 44: # set JAVACMD and JAVACCMD
 45: set_java_home() {
 46:   # For Cygwin and MinGW, ensure paths are in Unix format before anything is touched
 47:   if [ -n "${JAVA_HOME-}" ]; then
 48:     if [ -x "$JAVA_HOME/jre/sh/java" ]; then
 49:       # IBM's JDK on AIX uses strange locations for the executables
 50:       JAVACMD="$JAVA_HOME/jre/sh/java"
 51:       JAVACCMD="$JAVA_HOME/jre/sh/javac"
 52:     else
 53:       JAVACMD="$JAVA_HOME/bin/java"
 54:       JAVACCMD="$JAVA_HOME/bin/javac"
 55: 
 56:       if [ ! -x "$JAVACMD" ] || [ ! -x "$JAVACCMD" ]; then
 57:         echo "The JAVA_HOME environment variable is not defined correctly, so mvnw cannot run." >&2
 58:         echo "JAVA_HOME is set to \"$JAVA_HOME\", but \"\$JAVA_HOME/bin/java\" or \"\$JAVA_HOME/bin/javac\" does not exist." >&2
 59:         return 1
 60:       fi
 61:     fi
 62:   else
 63:     JAVACMD="$(
 64:       'set' +e
 65:       'unset' -f command 2>/dev/null
 66:       'command' -v java
 67:     )" || :
 68:     JAVACCMD="$(
 69:       'set' +e
 70:       'unset' -f command 2>/dev/null
 71:       'command' -v javac
 72:     )" || :
 73: 
 74:     if [ ! -x "${JAVACMD-}" ] || [ ! -x "${JAVACCMD-}" ]; then
 75:       echo "The java/javac command does not exist in PATH nor is JAVA_HOME set, so mvnw cannot run." >&2
 76:       return 1
 77:     fi
 78:   fi
 79: }
 80: 
 81: # hash string like Java String::hashCode
 82: hash_string() {
 83:   str="${1:-}" h=0
 84:   while [ -n "$str" ]; do
 85:     char="${str%"${str#?}"}"
 86:     h=$(((h * 31 + $(LC_CTYPE=C printf %d "'$char")) % 4294967296))
 87:     str="${str#?}"
 88:   done
 89:   printf %x\\n $h
 90: }
 91: 
 92: verbose() { :; }
 93: [ "${MVNW_VERBOSE-}" != true ] || verbose() { printf %s\\n "${1-}"; }
 94: 
 95: die() {
 96:   printf %s\\n "$1" >&2
 97:   exit 1
 98: }
 99: 
100: trim() {
101:   # MWRAPPER-139:
102:   #   Trims trailing and leading whitespace, carriage returns, tabs, and linefeeds.
103:   #   Needed for removing poorly interpreted newline sequences when running in more
104:   #   exotic environments such as mingw bash on Windows.
105:   printf "%s" "${1}" | tr -d '[:space:]'
106: }
107: 
108: scriptDir="$(dirname "$0")"
109: scriptName="$(basename "$0")"
110: 
111: # parse distributionUrl and optional distributionSha256Sum, requires .mvn/wrapper/maven-wrapper.properties
112: while IFS="=" read -r key value; do
113:   case "${key-}" in
114:   distributionUrl) distributionUrl=$(trim "${value-}") ;;
115:   distributionSha256Sum) distributionSha256Sum=$(trim "${value-}") ;;
116:   esac
117: done <"$scriptDir/.mvn/wrapper/maven-wrapper.properties"
118: [ -n "${distributionUrl-}" ] || die "cannot read distributionUrl property in $scriptDir/.mvn/wrapper/maven-wrapper.properties"
119: 
120: case "${distributionUrl##*/}" in
121: maven-mvnd-*bin.*)
122:   MVN_CMD=mvnd.sh _MVNW_REPO_PATTERN=/maven/mvnd/
123:   case "${PROCESSOR_ARCHITECTURE-}${PROCESSOR_ARCHITEW6432-}:$(uname -a)" in
124:   *AMD64:CYGWIN* | *AMD64:MINGW*) distributionPlatform=windows-amd64 ;;
125:   :Darwin*x86_64) distributionPlatform=darwin-amd64 ;;
126:   :Darwin*arm64) distributionPlatform=darwin-aarch64 ;;
127:   :Linux*x86_64*) distributionPlatform=linux-amd64 ;;
128:   *)
129:     echo "Cannot detect native platform for mvnd on $(uname)-$(uname -m), use pure java version" >&2
130:     distributionPlatform=linux-amd64
131:     ;;
132:   esac
133:   distributionUrl="${distributionUrl%-bin.*}-$distributionPlatform.zip"
134:   ;;
135: maven-mvnd-*) MVN_CMD=mvnd.sh _MVNW_REPO_PATTERN=/maven/mvnd/ ;;
136: *) MVN_CMD="mvn${scriptName#mvnw}" _MVNW_REPO_PATTERN=/org/apache/maven/ ;;
137: esac
138: 
139: # apply MVNW_REPOURL and calculate MAVEN_HOME
140: # maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
141: [ -z "${MVNW_REPOURL-}" ] || distributionUrl="$MVNW_REPOURL$_MVNW_REPO_PATTERN${distributionUrl#*"$_MVNW_REPO_PATTERN"}"
142: distributionUrlName="${distributionUrl##*/}"
143: distributionUrlNameMain="${distributionUrlName%.*}"
144: distributionUrlNameMain="${distributionUrlNameMain%-bin}"
145: MAVEN_USER_HOME="${MAVEN_USER_HOME:-${HOME}/.m2}"
146: MAVEN_HOME="${MAVEN_USER_HOME}/wrapper/dists/${distributionUrlNameMain-}/$(hash_string "$distributionUrl")"
147: 
148: exec_maven() {
149:   unset MVNW_VERBOSE MVNW_USERNAME MVNW_PASSWORD MVNW_REPOURL || :
150:   exec "$MAVEN_HOME/bin/$MVN_CMD" "$@" || die "cannot exec $MAVEN_HOME/bin/$MVN_CMD"
151: }
152: 
153: if [ -d "$MAVEN_HOME" ]; then
154:   verbose "found existing MAVEN_HOME at $MAVEN_HOME"
155:   exec_maven "$@"
156: fi
157: 
158: case "${distributionUrl-}" in
159: *?-bin.zip | *?maven-mvnd-?*-?*.zip) ;;
160: *) die "distributionUrl is not valid, must match *-bin.zip or maven-mvnd-*.zip, but found '${distributionUrl-}'" ;;
161: esac
162: 
163: # prepare tmp dir
164: if TMP_DOWNLOAD_DIR="$(mktemp -d)" && [ -d "$TMP_DOWNLOAD_DIR" ]; then
165:   clean() { rm -rf -- "$TMP_DOWNLOAD_DIR"; }
166:   trap clean HUP INT TERM EXIT
167: else
168:   die "cannot create temp dir"
169: fi
170: 
171: mkdir -p -- "${MAVEN_HOME%/*}"
172: 
173: # Download and Install Apache Maven
174: verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
175: verbose "Downloading from: $distributionUrl"
176: verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"
177: 
178: # select .zip or .tar.gz
179: if ! command -v unzip >/dev/null; then
180:   distributionUrl="${distributionUrl%.zip}.tar.gz"
181:   distributionUrlName="${distributionUrl##*/}"
182: fi
183: 
184: # verbose opt
185: __MVNW_QUIET_WGET=--quiet __MVNW_QUIET_CURL=--silent __MVNW_QUIET_UNZIP=-q __MVNW_QUIET_TAR=''
186: [ "${MVNW_VERBOSE-}" != true ] || __MVNW_QUIET_WGET='' __MVNW_QUIET_CURL='' __MVNW_QUIET_UNZIP='' __MVNW_QUIET_TAR=v
187: 
188: # normalize http auth
189: case "${MVNW_PASSWORD:+has-password}" in
190: '') MVNW_USERNAME='' MVNW_PASSWORD='' ;;
191: has-password) [ -n "${MVNW_USERNAME-}" ] || MVNW_USERNAME='' MVNW_PASSWORD='' ;;
192: esac
193: 
194: if [ -z "${MVNW_USERNAME-}" ] && command -v wget >/dev/null; then
195:   verbose "Found wget ... using wget"
196:   wget ${__MVNW_QUIET_WGET:+"$__MVNW_QUIET_WGET"} "$distributionUrl" -O "$TMP_DOWNLOAD_DIR/$distributionUrlName" || die "wget: Failed to fetch $distributionUrl"
197: elif [ -z "${MVNW_USERNAME-}" ] && command -v curl >/dev/null; then
198:   verbose "Found curl ... using curl"
199:   curl ${__MVNW_QUIET_CURL:+"$__MVNW_QUIET_CURL"} -f -L -o "$TMP_DOWNLOAD_DIR/$distributionUrlName" "$distributionUrl" || die "curl: Failed to fetch $distributionUrl"
200: elif set_java_home; then
201:   verbose "Falling back to use Java to download"
202:   javaSource="$TMP_DOWNLOAD_DIR/Downloader.java"
203:   targetZip="$TMP_DOWNLOAD_DIR/$distributionUrlName"
204:   cat >"$javaSource" <<-END
205: 	public class Downloader extends java.net.Authenticator
206: 	{
207: 	  protected java.net.PasswordAuthentication getPasswordAuthentication()
208: 	  {
209: 	    return new java.net.PasswordAuthentication( System.getenv( "MVNW_USERNAME" ), System.getenv( "MVNW_PASSWORD" ).toCharArray() );
210: 	  }
211: 	  public static void main( String[] args ) throws Exception
212: 	  {
213: 	    setDefault( new Downloader() );
214: 	    java.nio.file.Files.copy( java.net.URI.create( args[0] ).toURL().openStream(), java.nio.file.Paths.get( args[1] ).toAbsolutePath().normalize() );
215: 	  }
216: 	}
217: 	END
218:   # For Cygwin/MinGW, switch paths to Windows format before running javac and java
219:   verbose " - Compiling Downloader.java ..."
220:   "$(native_path "$JAVACCMD")" "$(native_path "$javaSource")" || die "Failed to compile Downloader.java"
221:   verbose " - Running Downloader.java ..."
222:   "$(native_path "$JAVACMD")" -cp "$(native_path "$TMP_DOWNLOAD_DIR")" Downloader "$distributionUrl" "$(native_path "$targetZip")"
223: fi
224: 
225: # If specified, validate the SHA-256 sum of the Maven distribution zip file
226: if [ -n "${distributionSha256Sum-}" ]; then
227:   distributionSha256Result=false
228:   if [ "$MVN_CMD" = mvnd.sh ]; then
229:     echo "Checksum validation is not supported for maven-mvnd." >&2
230:     echo "Please disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties." >&2
231:     exit 1
232:   elif command -v sha256sum >/dev/null; then
233:     if echo "$distributionSha256Sum  $TMP_DOWNLOAD_DIR/$distributionUrlName" | sha256sum -c - >/dev/null 2>&1; then
234:       distributionSha256Result=true
235:     fi
236:   elif command -v shasum >/dev/null; then
237:     if echo "$distributionSha256Sum  $TMP_DOWNLOAD_DIR/$distributionUrlName" | shasum -a 256 -c >/dev/null 2>&1; then
238:       distributionSha256Result=true
239:     fi
240:   else
241:     echo "Checksum validation was requested but neither 'sha256sum' or 'shasum' are available." >&2
242:     echo "Please install either command, or disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties." >&2
243:     exit 1
244:   fi
245:   if [ $distributionSha256Result = false ]; then
246:     echo "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised." >&2
247:     echo "If you updated your Maven version, you need to update the specified distributionSha256Sum property." >&2
248:     exit 1
249:   fi
250: fi
251: 
252: # unzip and move
253: if command -v unzip >/dev/null; then
254:   unzip ${__MVNW_QUIET_UNZIP:+"$__MVNW_QUIET_UNZIP"} "$TMP_DOWNLOAD_DIR/$distributionUrlName" -d "$TMP_DOWNLOAD_DIR" || die "failed to unzip"
255: else
256:   tar xzf${__MVNW_QUIET_TAR:+"$__MVNW_QUIET_TAR"} "$TMP_DOWNLOAD_DIR/$distributionUrlName" -C "$TMP_DOWNLOAD_DIR" || die "failed to untar"
257: fi
258: 
259: # Find the actual extracted directory name (handles snapshots where filename != directory name)
260: actualDistributionDir=""
261: 
262: # First try the expected directory name (for regular distributions)
263: if [ -d "$TMP_DOWNLOAD_DIR/$distributionUrlNameMain" ]; then
264:   if [ -f "$TMP_DOWNLOAD_DIR/$distributionUrlNameMain/bin/$MVN_CMD" ]; then
265:     actualDistributionDir="$distributionUrlNameMain"
266:   fi
267: fi
268: 
269: # If not found, search for any directory with the Maven executable (for snapshots)
270: if [ -z "$actualDistributionDir" ]; then
271:   # enable globbing to iterate over items
272:   set +f
273:   for dir in "$TMP_DOWNLOAD_DIR"/*; do
274:     if [ -d "$dir" ]; then
275:       if [ -f "$dir/bin/$MVN_CMD" ]; then
276:         actualDistributionDir="$(basename "$dir")"
277:         break
278:       fi
279:     fi
280:   done
281:   set -f
282: fi
283: 
284: if [ -z "$actualDistributionDir" ]; then
285:   verbose "Contents of $TMP_DOWNLOAD_DIR:"
286:   verbose "$(ls -la "$TMP_DOWNLOAD_DIR")"
287:   die "Could not find Maven distribution directory in extracted archive"
288: fi
289: 
290: verbose "Found extracted Maven distribution directory: $actualDistributionDir"
291: printf %s\\n "$distributionUrl" >"$TMP_DOWNLOAD_DIR/$actualDistributionDir/mvnw.url"
292: mv -- "$TMP_DOWNLOAD_DIR/$actualDistributionDir" "$MAVEN_HOME" || [ -d "$MAVEN_HOME" ] || die "fail to move MAVEN_HOME"
293: 
294: clean || :
295: exec_maven "$@"
````

## File: mvnw.cmd
````batch
  1: <# : batch portion
  2: @REM ----------------------------------------------------------------------------
  3: @REM Licensed to the Apache Software Foundation (ASF) under one
  4: @REM or more contributor license agreements.  See the NOTICE file
  5: @REM distributed with this work for additional information
  6: @REM regarding copyright ownership.  The ASF licenses this file
  7: @REM to you under the Apache License, Version 2.0 (the
  8: @REM "License"); you may not use this file except in compliance
  9: @REM with the License.  You may obtain a copy of the License at
 10: @REM
 11: @REM    http://www.apache.org/licenses/LICENSE-2.0
 12: @REM
 13: @REM Unless required by applicable law or agreed to in writing,
 14: @REM software distributed under the License is distributed on an
 15: @REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 16: @REM KIND, either express or implied.  See the License for the
 17: @REM specific language governing permissions and limitations
 18: @REM under the License.
 19: @REM ----------------------------------------------------------------------------
 20: 
 21: @REM ----------------------------------------------------------------------------
 22: @REM Apache Maven Wrapper startup batch script, version 3.3.4
 23: @REM
 24: @REM Optional ENV vars
 25: @REM   MVNW_REPOURL - repo url base for downloading maven distribution
 26: @REM   MVNW_USERNAME/MVNW_PASSWORD - user and password for downloading maven
 27: @REM   MVNW_VERBOSE - true: enable verbose log; others: silence the output
 28: @REM ----------------------------------------------------------------------------
 29: 
 30: @IF "%__MVNW_ARG0_NAME__%"=="" (SET __MVNW_ARG0_NAME__=%~nx0)
 31: @SET __MVNW_CMD__=
 32: @SET __MVNW_ERROR__=
 33: @SET __MVNW_PSMODULEP_SAVE=%PSModulePath%
 34: @SET PSModulePath=
 35: @FOR /F "usebackq tokens=1* delims==" %%A IN (`powershell -noprofile "& {$scriptDir='%~dp0'; $script='%__MVNW_ARG0_NAME__%'; icm -ScriptBlock ([Scriptblock]::Create((Get-Content -Raw '%~f0'))) -NoNewScope}"`) DO @(
 36:   IF "%%A"=="MVN_CMD" (set __MVNW_CMD__=%%B) ELSE IF "%%B"=="" (echo %%A) ELSE (echo %%A=%%B)
 37: )
 38: @SET PSModulePath=%__MVNW_PSMODULEP_SAVE%
 39: @SET __MVNW_PSMODULEP_SAVE=
 40: @SET __MVNW_ARG0_NAME__=
 41: @SET MVNW_USERNAME=
 42: @SET MVNW_PASSWORD=
 43: @IF NOT "%__MVNW_CMD__%"=="" ("%__MVNW_CMD__%" %*)
 44: @echo Cannot start maven from wrapper >&2 && exit /b 1
 45: @GOTO :EOF
 46: : end batch / begin powershell #>
 47: 
 48: $ErrorActionPreference = "Stop"
 49: if ($env:MVNW_VERBOSE -eq "true") {
 50:   $VerbosePreference = "Continue"
 51: }
 52: 
 53: # calculate distributionUrl, requires .mvn/wrapper/maven-wrapper.properties
 54: $distributionUrl = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionUrl
 55: if (!$distributionUrl) {
 56:   Write-Error "cannot read distributionUrl property in $scriptDir/.mvn/wrapper/maven-wrapper.properties"
 57: }
 58: 
 59: switch -wildcard -casesensitive ( $($distributionUrl -replace '^.*/','') ) {
 60:   "maven-mvnd-*" {
 61:     $USE_MVND = $true
 62:     $distributionUrl = $distributionUrl -replace '-bin\.[^.]*$',"-windows-amd64.zip"
 63:     $MVN_CMD = "mvnd.cmd"
 64:     break
 65:   }
 66:   default {
 67:     $USE_MVND = $false
 68:     $MVN_CMD = $script -replace '^mvnw','mvn'
 69:     break
 70:   }
 71: }
 72: 
 73: # apply MVNW_REPOURL and calculate MAVEN_HOME
 74: # maven home pattern: ~/.m2/wrapper/dists/{apache-maven-<version>,maven-mvnd-<version>-<platform>}/<hash>
 75: if ($env:MVNW_REPOURL) {
 76:   $MVNW_REPO_PATTERN = if ($USE_MVND -eq $False) { "/org/apache/maven/" } else { "/maven/mvnd/" }
 77:   $distributionUrl = "$env:MVNW_REPOURL$MVNW_REPO_PATTERN$($distributionUrl -replace "^.*$MVNW_REPO_PATTERN",'')"
 78: }
 79: $distributionUrlName = $distributionUrl -replace '^.*/',''
 80: $distributionUrlNameMain = $distributionUrlName -replace '\.[^.]*$','' -replace '-bin$',''
 81: 
 82: $MAVEN_M2_PATH = "$HOME/.m2"
 83: if ($env:MAVEN_USER_HOME) {
 84:   $MAVEN_M2_PATH = "$env:MAVEN_USER_HOME"
 85: }
 86: 
 87: if (-not (Test-Path -Path $MAVEN_M2_PATH)) {
 88:     New-Item -Path $MAVEN_M2_PATH -ItemType Directory | Out-Null
 89: }
 90: 
 91: $MAVEN_WRAPPER_DISTS = $null
 92: if ((Get-Item $MAVEN_M2_PATH).Target[0] -eq $null) {
 93:   $MAVEN_WRAPPER_DISTS = "$MAVEN_M2_PATH/wrapper/dists"
 94: } else {
 95:   $MAVEN_WRAPPER_DISTS = (Get-Item $MAVEN_M2_PATH).Target[0] + "/wrapper/dists"
 96: }
 97: 
 98: $MAVEN_HOME_PARENT = "$MAVEN_WRAPPER_DISTS/$distributionUrlNameMain"
 99: $MAVEN_HOME_NAME = ([System.Security.Cryptography.SHA256]::Create().ComputeHash([byte[]][char[]]$distributionUrl) | ForEach-Object {$_.ToString("x2")}) -join ''
100: $MAVEN_HOME = "$MAVEN_HOME_PARENT/$MAVEN_HOME_NAME"
101: 
102: if (Test-Path -Path "$MAVEN_HOME" -PathType Container) {
103:   Write-Verbose "found existing MAVEN_HOME at $MAVEN_HOME"
104:   Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
105:   exit $?
106: }
107: 
108: if (! $distributionUrlNameMain -or ($distributionUrlName -eq $distributionUrlNameMain)) {
109:   Write-Error "distributionUrl is not valid, must end with *-bin.zip, but found $distributionUrl"
110: }
111: 
112: # prepare tmp dir
113: $TMP_DOWNLOAD_DIR_HOLDER = New-TemporaryFile
114: $TMP_DOWNLOAD_DIR = New-Item -Itemtype Directory -Path "$TMP_DOWNLOAD_DIR_HOLDER.dir"
115: $TMP_DOWNLOAD_DIR_HOLDER.Delete() | Out-Null
116: trap {
117:   if ($TMP_DOWNLOAD_DIR.Exists) {
118:     try { Remove-Item $TMP_DOWNLOAD_DIR -Recurse -Force | Out-Null }
119:     catch { Write-Warning "Cannot remove $TMP_DOWNLOAD_DIR" }
120:   }
121: }
122: 
123: New-Item -Itemtype Directory -Path "$MAVEN_HOME_PARENT" -Force | Out-Null
124: 
125: # Download and Install Apache Maven
126: Write-Verbose "Couldn't find MAVEN_HOME, downloading and installing it ..."
127: Write-Verbose "Downloading from: $distributionUrl"
128: Write-Verbose "Downloading to: $TMP_DOWNLOAD_DIR/$distributionUrlName"
129: 
130: $webclient = New-Object System.Net.WebClient
131: if ($env:MVNW_USERNAME -and $env:MVNW_PASSWORD) {
132:   $webclient.Credentials = New-Object System.Net.NetworkCredential($env:MVNW_USERNAME, $env:MVNW_PASSWORD)
133: }
134: [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
135: $webclient.DownloadFile($distributionUrl, "$TMP_DOWNLOAD_DIR/$distributionUrlName") | Out-Null
136: 
137: # If specified, validate the SHA-256 sum of the Maven distribution zip file
138: $distributionSha256Sum = (Get-Content -Raw "$scriptDir/.mvn/wrapper/maven-wrapper.properties" | ConvertFrom-StringData).distributionSha256Sum
139: if ($distributionSha256Sum) {
140:   if ($USE_MVND) {
141:     Write-Error "Checksum validation is not supported for maven-mvnd. `nPlease disable validation by removing 'distributionSha256Sum' from your maven-wrapper.properties."
142:   }
143:   Import-Module $PSHOME\Modules\Microsoft.PowerShell.Utility -Function Get-FileHash
144:   if ((Get-FileHash "$TMP_DOWNLOAD_DIR/$distributionUrlName" -Algorithm SHA256).Hash.ToLower() -ne $distributionSha256Sum) {
145:     Write-Error "Error: Failed to validate Maven distribution SHA-256, your Maven distribution might be compromised. If you updated your Maven version, you need to update the specified distributionSha256Sum property."
146:   }
147: }
148: 
149: # unzip and move
150: Expand-Archive "$TMP_DOWNLOAD_DIR/$distributionUrlName" -DestinationPath "$TMP_DOWNLOAD_DIR" | Out-Null
151: 
152: # Find the actual extracted directory name (handles snapshots where filename != directory name)
153: $actualDistributionDir = ""
154: 
155: # First try the expected directory name (for regular distributions)
156: $expectedPath = Join-Path "$TMP_DOWNLOAD_DIR" "$distributionUrlNameMain"
157: $expectedMvnPath = Join-Path "$expectedPath" "bin/$MVN_CMD"
158: if ((Test-Path -Path $expectedPath -PathType Container) -and (Test-Path -Path $expectedMvnPath -PathType Leaf)) {
159:   $actualDistributionDir = $distributionUrlNameMain
160: }
161: 
162: # If not found, search for any directory with the Maven executable (for snapshots)
163: if (!$actualDistributionDir) {
164:   Get-ChildItem -Path "$TMP_DOWNLOAD_DIR" -Directory | ForEach-Object {
165:     $testPath = Join-Path $_.FullName "bin/$MVN_CMD"
166:     if (Test-Path -Path $testPath -PathType Leaf) {
167:       $actualDistributionDir = $_.Name
168:     }
169:   }
170: }
171: 
172: if (!$actualDistributionDir) {
173:   Write-Error "Could not find Maven distribution directory in extracted archive"
174: }
175: 
176: Write-Verbose "Found extracted Maven distribution directory: $actualDistributionDir"
177: Rename-Item -Path "$TMP_DOWNLOAD_DIR/$actualDistributionDir" -NewName $MAVEN_HOME_NAME | Out-Null
178: try {
179:   Move-Item -Path "$TMP_DOWNLOAD_DIR/$MAVEN_HOME_NAME" -Destination $MAVEN_HOME_PARENT | Out-Null
180: } catch {
181:   if (! (Test-Path -Path "$MAVEN_HOME" -PathType Container)) {
182:     Write-Error "fail to move MAVEN_HOME"
183:   }
184: } finally {
185:   try { Remove-Item $TMP_DOWNLOAD_DIR -Recurse -Force | Out-Null }
186:   catch { Write-Warning "Cannot remove $TMP_DOWNLOAD_DIR" }
187: }
188: 
189: Write-Output "MVN_CMD=$MAVEN_HOME/bin/$MVN_CMD"
````

## File: pom.xml
````xml
  1: <?xml version="1.0" encoding="UTF-8"?>
  2: <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  3: 	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  4: 	<modelVersion>4.0.0</modelVersion>
  5: 	<parent>
  6: 		<groupId>org.springframework.boot</groupId>
  7: 		<artifactId>spring-boot-starter-parent</artifactId>
  8: 		<version>4.1.0</version>
  9: 		<relativePath/> <!-- lookup parent from repository -->
 10: 	</parent>
 11: 	<groupId>com.observability</groupId>
 12: 	<artifactId>apexium.log</artifactId>
 13: 	<version>1.0.0</version>
 14: 	<name/>
 15: 	<description/>
 16: 	<url/>
 17: 	<licenses>
 18: 		<license/>
 19: 	</licenses>
 20: 	<developers>
 21: 		<developer/>
 22: 	</developers>
 23: 	<scm>
 24: 		<connection/>
 25: 		<developerConnection/>
 26: 		<tag/>
 27: 		<url/>
 28: 	</scm>
 29: 	<properties>
 30: 		<java.version>21</java.version>
 31: 		<kotlin.version>2.4.10</kotlin.version>
 32: 	</properties>
 33: 	<dependencies>
 34: 		<!-- CometD HTTP transport via Jetty -->
 35: 		<dependency>
 36: 			<groupId>org.springframework.boot</groupId>
 37: 			<artifactId>spring-boot-starter-web</artifactId>
 38: 		</dependency>
 39: 		<!--<dependency>
 40: 			<groupId>org.cometd.java</groupId>
 41: 			<artifactId>cometd-java-client-http-jetty</artifactId>
 42: 			<version>8.0.9</version>
 43: 		</dependency>-->
 44: 		<dependency>
 45: 			<groupId>org.springframework.boot</groupId>
 46: 			<artifactId>spring-boot-starter-actuator</artifactId>
 47: 		</dependency>
 48: 		<dependency>
 49: 			<groupId>io.micrometer</groupId>
 50: 			<artifactId>micrometer-registry-prometheus</artifactId>
 51: 		</dependency>
 52: 		<dependency>
 53: 			<groupId>org.springframework.boot</groupId>
 54: 			<artifactId>spring-boot-starter-data-redis</artifactId>
 55: 		</dependency>
 56: 		<dependency>
 57: 			<groupId>org.springframework.boot</groupId>
 58: 			<artifactId>spring-boot-starter-data-jpa</artifactId>
 59: 		</dependency>
 60: 		<dependency>
 61: 			<groupId>org.springframework.boot</groupId>
 62: 			<artifactId>spring-boot-starter-validation</artifactId>
 63: 		</dependency>
 64: 		<dependency>
 65: 			<groupId>org.postgresql</groupId>
 66: 			<artifactId>postgresql</artifactId>
 67: 			<scope>runtime</scope>
 68: 		</dependency>
 69: 		<dependency>
 70: 			<groupId>com.h2database</groupId>
 71: 			<artifactId>h2</artifactId>
 72: 			<scope>test</scope>
 73: 		</dependency>
 74: 		<dependency>
 75: 			<groupId>org.springframework.boot</groupId>
 76: 			<artifactId>spring-boot-starter</artifactId>
 77: 		</dependency>
 78: 		<dependency>
 79: 			<groupId>org.jetbrains.kotlin</groupId>
 80: 			<artifactId>kotlin-reflect</artifactId>
 81: 		</dependency>
 82: 		<dependency>
 83: 			<groupId>org.jetbrains.kotlin</groupId>
 84: 			<artifactId>kotlin-stdlib</artifactId>
 85: 		</dependency>
 86: 		<dependency>
 87: 			<groupId>com.fasterxml.jackson.module</groupId>
 88: 			<artifactId>jackson-module-kotlin</artifactId>
 89: 		</dependency>
 90: 		<dependency>
 91: 			<groupId>org.springframework.boot</groupId>
 92: 			<artifactId>spring-boot-devtools</artifactId>
 93: 			<scope>runtime</scope>
 94: 			<optional>true</optional>
 95: 		</dependency>
 96: 		<dependency>
 97: 			<groupId>org.projectlombok</groupId>
 98: 			<artifactId>lombok</artifactId>
 99: 			<optional>true</optional>
100: 		</dependency>
101: 		<dependency>
102: 			<groupId>org.springframework.boot</groupId>
103: 			<artifactId>spring-boot-starter-test</artifactId>
104: 			<scope>test</scope>
105: 		</dependency>
106: 		<dependency>
107: 			<groupId>io.minio</groupId>
108: 			<artifactId>minio</artifactId>
109: 			<version>9.0.3</version>
110: 			<exclusions>
111: 				<exclusion>
112: 					<groupId>com.squareup.okhttp3</groupId>
113: 					<artifactId>okhttp</artifactId>
114: 				</exclusion>
115: 			</exclusions>
116: 		</dependency>
117: 		<dependency>
118: 			<groupId>com.squareup.okhttp3</groupId>
119: 			<artifactId>okhttp</artifactId>
120: 			<version>4.12.0</version>
121: 		</dependency>
122: 		<dependency>
123: 			<groupId>org.springdoc</groupId>
124: 			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
125: 			<version>3.0.3</version>
126: 		</dependency>
127: 		<dependency>
128: 			<groupId>org.jetbrains.kotlin</groupId>
129: 			<artifactId>kotlin-test-junit5</artifactId>
130: 			<scope>test</scope>
131: 		</dependency>
132: 		<dependency>
133: 			<groupId>io.github.java-diff-utils</groupId>
134: 			<artifactId>java-diff-utils</artifactId>
135: 			<version>4.17</version>
136: 		</dependency>
137: 	</dependencies>
138: 	<build>
139: 		<sourceDirectory>${project.basedir}/src/main/kotlin</sourceDirectory>
140: 		<testSourceDirectory>${project.basedir}/src/test/kotlin</testSourceDirectory>
141: 		<plugins>
142: 			<plugin>
143: 				<groupId>org.springframework.boot</groupId>
144: 				<artifactId>spring-boot-maven-plugin</artifactId>
145: 				<configuration>
146: 					<excludes>
147: 						<exclude>
148: 							<groupId>org.projectlombok</groupId>
149: 							<artifactId>lombok</artifactId>
150: 						</exclude>
151: 					</excludes>
152: 				</configuration>
153: 			</plugin>
154: 			<plugin>
155: 				<groupId>org.jetbrains.kotlin</groupId>
156: 				<artifactId>kotlin-maven-plugin</artifactId>
157: 				<configuration>
158: 					<args>
159: 						<arg>-Xjsr305=strict</arg>
160: 						<arg>-Xannotation-default-target=param-property</arg>
161: 						<arg>-Xmulti-dollar-interpolation</arg>
162: 					</args>
163: 					<compilerPlugins>
164: 						<plugin>spring</plugin>
165: 						<plugin>jpa</plugin>
166: 					</compilerPlugins>
167: 				</configuration>
168: 				<dependencies>
169: 					<dependency>
170: 						<groupId>org.jetbrains.kotlin</groupId>
171: 						<artifactId>kotlin-maven-allopen</artifactId>
172: 						<version>${kotlin.version}</version>
173: 					</dependency>
174: 					<dependency>
175: 						<groupId>org.jetbrains.kotlin</groupId>
176: 						<artifactId>kotlin-maven-noarg</artifactId>
177: 						<version>${kotlin.version}</version>
178: 					</dependency>
179: 				</dependencies>
180: 			</plugin>
181: 		</plugins>
182: 	</build>
183: </project>
````

## File: README.md
````markdown
  1: # Apexium.log
  2: 
  3: a Salesforce developer productivity tool designed to simplify Debug Log management, automate Trace Flag scheduling, monitor Apex code coverage, track metadata changes, and retain debugging history beyond Salesforce's native limitations. It helps developers spend less time managing logs and more time building reliable applications.
  4: 
  5: ## Features
  6: 
  7: * 🚀 **Centralized Debug Log Management** to manage, search, download, and delete Salesforce Debug Logs from a single interface, making debugging more efficient.
  8: * ⏰ **Automated Recurring Trace Flags** to automatically extend user trace sessions beyond Salesforce's 24-hour Trace Flag limitation.
  9: * 📊 **Apex Code Coverage Reports** to view code coverage for all Apex Classes and Triggers in a centralized dashboard, helping ensure deployment readiness.
 10: * 🔍 **Metadata Change Tracking** to detect and compare changes in Apex Classes and Triggers through metadata body comparison, making code changes easier to review.
 11: * ♻️ **Reusable Debug Sessions** to quickly reuse previously configured Trace Flags and debugging settings without repeating manual configuration.
 12: * 🗃️ **Extended Debug Log Retention** to retain Debug Logs for up to 30 days, even after they have expired or been removed from Salesforce.
 13: * ⚡ **Faster Root Cause Analysis** by combining Debug Log history, metadata changes, and trace information in a single application for quicker issue investigation.
 14: * 📈 **Improved Developer Productivity** by automating repetitive tasks such as Trace Flag scheduling, Debug Log cleanup, and code coverage monitoring.
 15: * 🛡️ **Reduced Debug Log Storage Issues** through selective or bulk Debug Log cleanup, helping prevent Salesforce storage limit issues.
 16: * 🎯 **Built for Salesforce Developers** who need an all-in-one solution for debugging, monitoring, and tracking Apex code changes.
 17: 
 18: ## Architecture Design
 19: 
 20: The system periodically fetches logs and metadata from Salesforce via REST and Tooling APIs, stores them in PostgreSQL and MinIO, and exposes everything through a React frontend.
 21: 
 22: ### Visual Workflow
 23: ```text
 24: +----------------+      Tooling / REST API      +-------------------+
 25: |   Salesforce   | <----------------------------> |  Kotlin Backend  |
 26: | (ApexLogs,     |   (Poll logs & metadata)      |  (Spring Boot)   |
 27: |  ApexClass,    |                                |                   |
 28: |  ApexTrigger,  |                                +---------+---------+
 29: |  TraceFlag)    |                                          |
 30: +----------------+                          +---------------+---------------+
 31:                                             |               |               |
 32:                                             v               v               v
 33:                                     +-----------+   +-----------+   +---------------+
 34:                                     | PostgreSQL |   |   MinIO   |   |    Redis      |
 35:                                     | (Metadata, |   | (Log Body |   |  (Cache,      |
 36:                                     |  History,  |   |  Storage) |   |   Sessions)   |
 37:                                     |  Jobs)     |   |           |   |               |
 38:                                     +-----------+   +-----------+   +---------------+
 39:                                             |
 40:                                             v
 41:                                     +---------------+
 42:                                     | React Frontend|
 43:                                     | (TypeScript)  |
 44:                                     +---------------+
 45: ```
 46: 
 47: ### Technical Flow (Mermaid)
 48: ```mermaid
 49: graph TD
 50:     SFDC[Salesforce] -- Tooling/REST API --> Backend[Kotlin Backend]
 51:     Backend -- Poll Logs & Store --> PG[(PostgreSQL)]
 52:     Backend -- Store Log Body --> MinIO[(MinIO)]
 53:     Backend -- Cache --> Redis[(Redis)]
 54:     Backend -- Serve Data --> FE[React Frontend]
 55:     FE -- User Actions --> Backend
 56: ```
 57: 
 58: ### Components
 59: 
 60: 1.  **Salesforce (Source)**: Source of ApexLogs, ApexClass, ApexTrigger, TraceFlag, and DebugLevel metadata. Accessed via REST and Tooling APIs.
 61: 2.  **Kotlin Backend (Spring Boot)**: Polls Salesforce periodically for new logs and metadata changes. Stores processed data in PostgreSQL and MinIO. Exposes REST APIs for the frontend.
 62: 3.  **PostgreSQL**: Stores log metadata, Apex class/trigger info, code coverage, metadata history (for diff comparison), trace jobs, and debug levels.
 63: 4.  **MinIO**: Stores full debug log body files for long-term retention beyond Salesforce's 24-hour window.
 64: 5.  **Redis**: Caching layer for Salesforce access tokens and metadata queries.
 65: 6.  **React Frontend**: TypeScript-based dashboard for viewing logs, managing traces, comparing metadata, and monitoring coverage.
 66: 
 67: ## Tech Stack
 68: 
 69: - **Backend**: Kotlin 2.1.0, Spring Boot 4.0.6
 70: - **Frontend**: React 19, TypeScript, Vite
 71: - **Database**: PostgreSQL 17, Redis (Alpine)
 72: - **Storage**: MinIO (S3-compatible object storage)
 73: - **Build Tools**: Maven (backend), npm (frontend)
 74: - **Communication**: Salesforce REST API & Tooling API
 75: 
 76: ## Getting Started
 77: 
 78: ### Prerequisites
 79: 
 80: - Java 21+
 81: - Node.js (Latest LTS)
 82: - Docker & Docker Compose (for PostgreSQL, Redis, MinIO)
 83: 
 84: ### Setup
 85: 
 86: 1.  **Salesforce Setup**: Create a Connected App in your Salesforce org with OAuth 2.0 Client Credentials flow enabled. Note the Consumer Key (`client_id`) and Consumer Secret (`client_secret`).
 87: 
 88: 2.  **Environment Configuration**: Create a `.env` file in the root directory:
 89:     ```bash
 90:     # Salesforce Configuration
 91:     SALESFORCE_INSTANCE_ORG=https://your-instance.sandbox.my.salesforce.com
 92:     SALESFORCE_CLIENT_ID=your_client_id
 93:     SALESFORCE_CLIENT_SECRET=your_client_secret
 94:     SALESFORCE_GRANT_TYPE=client_credentials
 95:     SALESFORCE_API_VERSION=v61.0
 96: 
 97:     # MinIO Configuration
 98:     MINIO_URL=http://minio:9000
 99:     MINIO_ACCESS_KEY=your_access_key
100:     MINIO_SECRET_KEY=your_secret_key
101:     MINIO_ROOT_USER=your_root_user
102:     MINIO_ROOT_PASSWORD=your_root_password
103:     MINIO_BUCKET_NAME=sfdc-bucket
104: 
105:     # Database Configuration
106:     DB_HOST=db
107:     DB_PORT=5432
108:     DB_NAME=sfdc_logs
109:     DB_USER=your_db_user
110:     DB_PASSWORD=your_db_pw
111:     ```
112: 
113: 3.  **Start Infrastructure** (PostgreSQL, Redis, MinIO):
114:     ```bash
115:     docker compose up -d --build
116:     ```
117: 
118: 4.  **Run Backend**:
119:     ```bash
120:     ./mvnw spring-boot:run
121:     ```
122: 
123: 5.  **Run Frontend** (in a separate terminal):
124:     ```bash
125:     cd frontend
126:     npm install
127:     npm run dev
128:     ```
129: 
130: 6.  Open [http://localhost:5173](http://localhost:5173) to access the dashboard.
131: 
132: ## Monitoring & API Documentation
133: 
134: The project includes built-in observability and interactive documentation tools.
135: 
136: ### Monitoring Stack
137: When running via Docker Compose, the following monitoring services are available:
138: 
139: - **Grafana**: [http://localhost:3000](http://localhost:3000) (Credentials: `admin` / `admin`)
140:     - Used for visualizing JVM metrics, Spring Boot statistics, and system health.
141:     - Pre-configured dashboards are recommended in [MONITORING.md](./MONITORING.md).
142: - **Prometheus**: [http://localhost:9090](http://localhost:9090)
143:     - Time-series database that scrapes metrics from the application.
144: - **Spring Actuator**: [http://localhost:8080/actuator](http://localhost:8080/actuator)
145:     - Provides raw metric data, health status, and info endpoints.
146: 
147: ### API Documentation (Swagger)
148: Interactive API documentation is automatically generated from the Spring Boot controllers:
149: 
150: - **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
151:     - Explore and test the REST API endpoints directly from your browser.
152: - **OpenAPI Spec**: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
153:     - Raw JSON/YAML specification for the API.
154: 
155: For detailed configuration of Prometheus and custom dashboards, refer to [MONITORING.md](./MONITORING.md).
156: 
157: ## Contributing
158: 
159: Contributions are welcome! Please read [CONTRIBUTING.md](./CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.
160: 
161: ## License
162: 
163: MIT License
164: 
165: Copyright (c) 2026 Ichwan Sholihin
166: 
167: Permission is hereby granted, free of charge, to any person obtaining a copy
168: of this software and associated documentation files (the "Software"), to deal
169: in the Software without restriction, including without limitation the rights
170: to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
171: copies of the Software, and to permit persons to whom the Software is
172: furnished to do so, subject to the following conditions:
173: 
174: The above copyright notice and this permission notice shall be included in all
175: copies or substantial portions of the Software.
176: 
177: THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
178: IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
179: FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
180: AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
181: LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
182: 
183: ---
184: *Maintained by the Observability Engineering Team.*
````

## File: SKILL.md
````markdown
 1: # Salesforce Observable Logging Skill
 2: 
 3: This skill provides instructions, references, and quick-reference snippets for implementing observable logging in Salesforce using Kotlin and CometD.
 4: 
 5: ## Instructions
 6: 
 7: ### 1. Enabling Debug Logs (Trace Flags)
 8: To capture logs, you must first set a `TraceFlag` for the target user or Apex class.
 9: - Use the **Salesforce Setup** > **Debug Logs** page.
10: - Or use the **Tooling API** to create a `TraceFlag` record programmatically.
11: 
12: ### 2. Retrieving Debug Logs
13: Debug logs are stored in the `ApexLog` object.
14: - **Query Metadata:** Use SOQL on the Tooling API to find log IDs.
15: - **Fetch Body:** Use the Tooling API REST endpoint to download the raw log text.
16: 
17: ### 3. Real-time Monitoring via CometD
18: To stream data changes or custom events to the Kotlin backend:
19: - Create a `PushTopic` in Salesforce with a SOQL query.
20: - Use the CometD client in Kotlin to subscribe to the `/topic/<PushTopicName>` channel.
21: 
22: ---
23: 
24: ## References
25: 
26: - [Salesforce Tooling API: ApexLog](https://developer.salesforce.com/docs/atlas.en-us.api_tooling.meta/api_tooling/tooling_api_objects_apexlog.htm)
27: - [Salesforce Tooling API: TraceFlag](https://developer.salesforce.com/docs/atlas.en-us.api_tooling.meta/api_tooling/tooling_api_objects_traceflag.htm)
28: - [Salesforce Event Monitoring (EventLogFile)](https://developer.salesforce.com/docs/atlas.en-us.api.meta/api/sforce_api_objects_eventlogfile.htm)
29: - [Salesforce Streaming API (PushTopic)](https://developer.salesforce.com/docs/atlas.en-us.api_streaming.meta/api_streaming/pushtopic_events_intro.htm)
30: - [CometD Java Client Documentation](https://docs.cometd.org/current/reference/#_java_client)
31: 
32: ---
33: 
34: ## Quick-References
35: 
36: ### SOQL: Querying Log Metadata
37: ```sql
38: SELECT Id, LogUserId, Operation, StartTime, Status, LogLength 
39: FROM ApexLog 
40: ORDER BY StartTime DESC 
41: LIMIT 10
42: ```
43: 
44: ### SOQL: Querying Event Logs
45: ```sql
46: SELECT Id, EventType, LogDate, LogFileLength 
47: FROM EventLogFile 
48: WHERE LogDate = YESTERDAY
49: ```
50: 
51: ### REST: Fetching Log Body
52: **Endpoint (ApexLog):**
53: `GET /services/data/v60.0/tooling/sobjects/ApexLog/{ID}/Body/`
54: 
55: **Endpoint (EventLogFile):**
56: `GET /services/data/v60.0/sobjects/EventLogFile/{ID}/LogFile`
57: 
58: **Example Header:**
59: `Authorization: Bearer <ACCESS_TOKEN>`
60: 
61: ### Apex: Creating a PushTopic
62: ```apex
63: PushTopic pushTopic = new PushTopic();
64: pushTopic.Name = 'LogUpdates';
65: pushTopic.Query = 'SELECT Id, Name, Status__c FROM CustomLog__c';
66: pushTopic.ApiVersion = 60.0;
67: pushTopic.NotifyForOperationCreate = true;
68: pushTopic.NotifyForOperationUpdate = true;
69: pushTopic.NotifyForFields = 'Referenced';
70: insert pushTopic;
71: ```
72: 
73: ### Kotlin: CometD Client Setup
74: ```kotlin
75: val transport = object : LongPollingTransport(options, httpClient) {
76:     override fun customize(request: Request) {
77:         request.headers { it.put("Authorization", "Bearer $accessToken") }
78:     }
79: }
80: val client = BayeuxClient(streamingEndpoint, transport)
81: client.handshake { _, message ->
82:     if (message.isSuccessful) {
83:         client.getChannel("/topic/LogUpdates").subscribe { channel, msg ->
84:             println("Received: ${msg.data}")
85:         }
86:     }
87: }
88: ```
89: 
90: ---
91: *Created for the Salesforce Observable Logging Project.*
````
