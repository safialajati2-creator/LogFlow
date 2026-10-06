# LogFlow

LogFlow Increment 2, Common Log Format satırlarını değişmez `LogRecord` nesnelerine dönüştüren ve bu kayıtları konsola yazdıran bir Java pipeline uygulamasıdır. Hatalı satırlar bu haftanın kuralına göre atlanır ve sayılır.

## Gereksinimler

- Java JDK 21 veya daha yeni bir sürüm

## Proje yapısı

- `src/Emitter.java`: Bir öğeyi sonraki bileşene gönderir.
- `src/Source.java`: Veri üreten bileşenlerin arayüzüdür.
- `src/Sink.java`: Veri tüketen bileşenlerin arayüzüdür.
- `src/Record.java`: Gelecek aşamalardaki kayıt türleri için işaretleyici arayüzdür.
- `src/Stage.java`: Genel işlem aşaması arayüzü ve yaşam döngüsü metotlarıdır.
- `src/FileLineSource.java`: Dosyadan her satırı bir `String` olarak okur.
- `src/LogRecord.java`: Zaman damgası, istemci IP'si, istek ve yanıt alanlarını taşıyan immutable domain kaydıdır.
- `src/ParserStage.java`: CLF/Combined Log Format satırlarını `LogRecord` nesnelerine dönüştürür; hatalı satırları atlar ve sayar.
- `src/ConsoleSink.java`: Gelen `LogRecord` değerini okunabilir tek satır olarak yazdırır.
- `src/Pipeline.java`: Source, sıralı aşamalar ve sink arasındaki bağlantıyı kurar.
- `src/Main.java`: Komut satırı parametresini alır, pipeline'ı kurar ve çalıştırır.
- `data/access-small.log`: Örnek giriş log dosyasıdır.

## Testler

ParserStage için JUnit 5 ile dosya sistemine dokunmayan 9 test durumu vardır. Testler geçerli kayıt, eksik alan, hatalı zaman damgası, hatalı durum kodu, boş satır, fazladan boşluk, boşluk içeren user agent, sorgu dizesi ve hatalı kayıt sayacını kapsar. Toplayıcı `Emitter` test dublörü kullanılır.

```bash
mvn test
```

Kapsama özeti: gerekli 8 senaryo ve hata sayacı dahil 9/9 test durumu kapsanmıştır (%100 senaryo kapsamı). JaCoCo test çalıştırmasında `target/site/jacoco/index.html` dosyasını üretir; kesin satır yüzdesi bu raporda gösterilir.

## Derleme

Proje ana klasöründe çalıştırın:

```bash
javac -d out src/*.java
```

## Çalıştırma

```bash
java -cp out Main data/access-small.log
```

## Beklenen sonuç

`data/access-small.log` dosyasındaki geçerli satırlar yapılandırılmış biçimde yazdırılır. Son satırda atlanan hatalı kayıt sayısı gösterilir.

## Değişiklik günlüğü

- `LogRecord` immutable domain modeli eklendi.
- `ParserStage` ile CLF/Combined Log Format ayrıştırması ve hatalı satır sayacı eklendi.
- `ConsoleSink` artık `LogRecord` yazdırıyor.
- ParserStage için JUnit 5 test paketi eklendi.
- Maven ve JaCoCo yapılandırması eklendi.
