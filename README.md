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
    elf -> user: User (nickname, address) to send a gift
else some user(s) doesn't have match
    elf -> admin: Show users without a match
    
    alt let's continue
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
First of all, you need to specify two parameters `bot.token` and `bot.username`. You can do it in the configuration file 
(don't forget to remove them before commit), or via environment variables. 
See the Spring boot [documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.external-config).