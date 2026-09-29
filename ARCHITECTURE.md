# Mimari

İlk aşamada bilinçli olarak küçük bir pipeline kurulmuştur. Source verileri üretir, pipeline aşamaları sıralı olarak işler ve sink verileri tüketir.

```mermaid
flowchart LR
    A[FileLineSource\nreads one line] --> B[Pipeline\nordered stages] --> C[ConsoleSink\nprints one line]
```

`Pipeline` aşamaları bir liste olarak saklar ve source ile sink arasındaki bağlantıyı kurar. `Main` yalnızca komut satırı parametresini okur, bileşenleri bir araya getirir ve `run()` metodunu çağırır.

`Emitter` soyutlaması kullanılmıştır; çünkü ilerleyen aşamalarda tek bir giriş sıfır, bir veya birden fazla çıktı üretebilir. Bir aşamadan tek bir değer döndürmek gereksiz bir bire bir ilişki oluştururdu.
