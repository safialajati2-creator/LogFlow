# Değişiklik Günlüğü

## v2

Increment 2 kapsamında değiştirilen ve eklenen dosyalar:

- `src/LogRecord.java` - Immutable alan kaydı.
- `src/ParserStage.java` - CLF/Combined Log Format ayrıştırıcısı ve hatalı kayıt sayacı.
- `src/ConsoleSink.java` - `LogRecord` değerlerini tek satırda yazdırma.
- `src/Pipeline.java` - Tipli aşama listesi ve kaynak-mesaj akışı.
- `src/Main.java` - ParserStage eklenmesi ve hatalı kayıt sayısının gösterilmesi.
- `src/Record.java` - Domain kayıtları için ortak marker arayüzü.
- `src/test/java/ParserStageTest.java` - JUnit 5 parser testleri.
- `pom.xml` - JUnit 5, Maven ve JaCoCo yapılandırması.
- `data/access-small.log` - Combined Log Format örnek verileri.
- `README.md` - Çalıştırma, test ve coverage açıklamaları.
- `ARCHITECTURE.md` - Increment 2 mimari açıklaması.
- `.gitignore` - Maven `target` klasörü.
- `src/Pipeline.java` - Source → ParserStage → Sink bağlantısı açık ve sıralı olacak şekilde sadeleştirildi.
- `src/Main.java` - Pipeline typed bileşenlerle doğrudan kuruluyor.
