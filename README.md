# junit-comparison-provider-issues

JUnit из [PR #6127](https://github.com/junit-team/junit-framework/pull/6127) (`TestTemplateComparisonProvider`)
превращает каждый вызов шаблона теста в контейнер. Внутри этого контейнера тест выполняется по разу для каждой
реализации. Короткие тесты показывают две группы последствий:

- [`demo/counting`](src/test/java/demo/counting): внешний шаблон считает реализации своими тестами — повтор JUnit
  Pioneer `@RetryingTest` и порог `failureThreshold` у `@RepeatedTest`;
- [`demo/rerun`](src/test/java/demo/rerun): запуск одного набора аргументов через `IterationSelector`.

Нужны git, JDK 25 (на нём собирается JUnit) и Maven 3.9+.

```bash
scripts/build-junit-pr.sh      # собирает JUnit из PR в ./junit-repo (версия 6.2.0-pr6127-SNAPSHOT), несколько минут
mvn test                       # все тесты, кроме RerunSuite
mvn test -Dtest=RerunSuite     # только RerunSuite
```

`mvn test` заканчивается `BUILD FAILURE`: падения входят в сценарии.
