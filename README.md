# vas3k-santa

## Description
Telegram bot - elf, to help Santa

## Basic sequence diagram
```plantuml
actor "Admin" as admin #99FF99
actor "User" as user #99FF99
entity "Elf-bot" as elf
participant "vas3k-auth" as auth #649cf5
participant "Database" as db #cb56d6

' Authentication is only for the first time
== Authentication ==
user -> elf: Auth
elf -> auth: Auth
'considering only positive case, to keep the diagram simple
user -> auth: Enter credentials
auth -> elf: Success
elf -> db: Save user
elf -> user: Success


== Participation request ==
user -> elf: Register me
elf -> elf: Check if user is allowed to participate
alt allowed
    elf -> user: Request data (region, address)
    user -> elf: Data
    elf -> db: Save data
    elf -> user: Success
else do not allowed
    elf -> user: Sorry message
end


== Shuffle ==

admin -> elf: Shuffle
note left: Day X

elf -> elf: Do the magic

alt all users have a matched user
    elf -> db: Save matches
    elf -> user: User (nickname, address) to send a gift
else some user(s) doesn't have match
    elf -> admin: Show users without a match
    
    alt let's continue
        elf -> db: Save matches
        elf -> user: User (nickname, address) to send a gift
    end
end

== Confirmation ==

user -> elf: The gift has received
elf -> db: Received
elf -> user: Thanks choom


```

## Development
### How to run
List of required properties:

- `app.bot.token` (`APP_BOT_TOKEN`) - bot token, you get it when you register your bot on Telegram
- `app.bot.username` (`APP_BOT_USERNAME`) - bot username, you get it when you register your bot on Telegram
- `spring.security.oauth2.client.registration.vas3k-reg.client-id` (`SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_VAS3K-REG_CLIENT-ID`) -  OAuth Client ID, you get it when you register your application on [vas3k.apps](https://vas3k.club/apps/)
- `spring.security.oauth2.client.registration.vas3k-reg.client-secret` (`SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_VAS3K-REG_CLIENT-SECRET`) - OAuth Client Secret, you get it when you register your application on [vas3k.apps](https://vas3k.club/apps/)
- `spring.datasource.username` (`SPRING_DATASOURCE_USERNAME`) - Username for the database
- `spring.datasource.password` (`SPRING_DATASOURCE_PASSWORD`) - Password for the database
- `spring.datasource.url` (`SPRING_DATASOURCE_URL`) - URL for the database, e.g.: `jdbc:postgresql://172.19.0.2:5432/santa?createDatabaseIfNotExist=true`


You can specify them in the configuration file (don't forget to remove them before commit), 
or via environment variables. See the Spring boot [documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config).