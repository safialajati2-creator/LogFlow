# LogFlow

LogFlow, Java ile yazılmış basit bir pipeline uygulamasıdır. Uygulama bir log dosyasını satır satır okur ve her satırı ayrı source, pipeline ve sink bileşenleri üzerinden konsola yazdırır.

## Gereksinimler

- Java JDK 21 veya daha yeni bir sürüm

## Proje yapısı

- `src/Emitter.java`: Bir öğeyi sonraki bileşene gönderir.
- `src/Source.java`: Veri üreten bileşenlerin arayüzüdür.
- `src/Sink.java`: Veri tüketen bileşenlerin arayüzüdür.
- `src/Record.java`: Gelecek aşamalardaki kayıt türleri için işaretleyici arayüzdür.
- `src/Stage.java`: Genel işlem aşaması arayüzü ve yaşam döngüsü metotlarıdır.
- `src/FileLineSource.java`: Dosyadan her satırı bir `String` olarak okur.
- `src/ConsoleSink.java`: Gelen her satırı konsola yazdırır.
- `src/Pipeline.java`: Aşamaları sıralı bir liste olarak saklar ve source ile sink arasındaki bağlantıyı kurar.
- `src/Main.java`: Komut satırı parametresini alır, pipeline'ı kurar ve çalıştırır.
- `data/access-small.log`: Örnek giriş log dosyasıdır.

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

`data/access-small.log` dosyasındaki tüm satırlar aynı sırayla konsola yazdırılır.
