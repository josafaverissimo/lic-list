# Lic-List

## Env vars

In dev, do not create .env files inside projects. Be creative,
inject env vars to process.

Example:

```nushell
overlay use lic-list-crawlers-env

./gradlew :projects:crawlers:run
```
