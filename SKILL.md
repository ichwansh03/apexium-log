# Salesforce Observable Logging Skill

This skill provides instructions, references, and quick-reference snippets for implementing observable logging in Salesforce using Kotlin and CometD.

## Instructions

### 1. Enabling Debug Logs (Trace Flags)
To capture logs, you must first set a `TraceFlag` for the target user or Apex class.
- Use the **Salesforce Setup** > **Debug Logs** page.
- Or use the **Tooling API** to create a `TraceFlag` record programmatically.

### 2. Retrieving Debug Logs
Debug logs are stored in the `ApexLog` object.
- **Query Metadata:** Use SOQL on the Tooling API to find log IDs.
- **Fetch Body:** Use the Tooling API REST endpoint to download the raw log text.

### 3. Real-time Monitoring via CometD
To stream data changes or custom events to the Kotlin backend:
- Create a `PushTopic` in Salesforce with a SOQL query.
- Use the CometD client in Kotlin to subscribe to the `/topic/<PushTopicName>` channel.

---

## References

- [Salesforce Tooling API: ApexLog](https://developer.salesforce.com/docs/atlas.en-us.api_tooling.meta/api_tooling/tooling_api_objects_apexlog.htm)
- [Salesforce Tooling API: TraceFlag](https://developer.salesforce.com/docs/atlas.en-us.api_tooling.meta/api_tooling/tooling_api_objects_traceflag.htm)
- [Salesforce Event Monitoring (EventLogFile)](https://developer.salesforce.com/docs/atlas.en-us.api.meta/api/sforce_api_objects_eventlogfile.htm)
- [Salesforce Streaming API (PushTopic)](https://developer.salesforce.com/docs/atlas.en-us.api_streaming.meta/api_streaming/pushtopic_events_intro.htm)
- [CometD Java Client Documentation](https://docs.cometd.org/current/reference/#_java_client)

---

## Quick-References

### SOQL: Querying Log Metadata
```sql
SELECT Id, LogUserId, Operation, StartTime, Status, LogLength 
FROM ApexLog 
ORDER BY StartTime DESC 
LIMIT 10
```

### SOQL: Querying Event Logs
```sql
SELECT Id, EventType, LogDate, LogFileLength 
FROM EventLogFile 
WHERE LogDate = YESTERDAY
```

### REST: Fetching Log Body
**Endpoint (ApexLog):**
`GET /services/data/v60.0/tooling/sobjects/ApexLog/{ID}/Body/`

**Endpoint (EventLogFile):**
`GET /services/data/v60.0/sobjects/EventLogFile/{ID}/LogFile`

**Example Header:**
`Authorization: Bearer <ACCESS_TOKEN>`

### Apex: Creating a PushTopic
```apex
PushTopic pushTopic = new PushTopic();
pushTopic.Name = 'LogUpdates';
pushTopic.Query = 'SELECT Id, Name, Status__c FROM CustomLog__c';
pushTopic.ApiVersion = 60.0;
pushTopic.NotifyForOperationCreate = true;
pushTopic.NotifyForOperationUpdate = true;
pushTopic.NotifyForFields = 'Referenced';
insert pushTopic;
```

### Kotlin: CometD Client Setup
```kotlin
val transport = object : LongPollingTransport(options, httpClient) {
    override fun customize(request: Request) {
        request.headers { it.put("Authorization", "Bearer $accessToken") }
    }
}
val client = BayeuxClient(streamingEndpoint, transport)
client.handshake { _, message ->
    if (message.isSuccessful) {
        client.getChannel("/topic/LogUpdates").subscribe { channel, msg ->
            println("Received: ${msg.data}")
        }
    }
}
```

---
*Created for the Salesforce Observable Logging Project.*
