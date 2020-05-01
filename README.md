# WAT
Bugün Ne Yedim Android Mobil Uygulaması

# Sürüm notları v2.0
- Açılış ekranı UserRegister olarak ayarlandı. (Artık MainActivity'deki saçma buton tasarımları kullanılmayacak)
- SearchActivity tasarlandı. Görmek için Sabah öğününün yanındaki '+' ikonuna tıklayın
- Sayfa navigasyonları(geçişleri) tasarlandı.
- Giriş yapan kullanıcı, GuestActivity'deki formu doldurduysa, uygulamayı bir daha açtığında tekrar form doldurmayacak
(SharedPreferences ile yapıldı)
- GuestActivity'deki çökmeler giderildi, tasarım düzenlendi
- Besinler için yeni class yazıldı
- UserRegister'daki çökmeler giderildi
- Besinler için adapter yazıldı
- OgunActivity'deki kullanıcı çıkış yaptığındaki çökmeler giderildi. (SP silindi)
- Ogunler için class yazıldı
- Bugünün tarihini tutan fonksiyon yazıldı(GuestActivity - getBugun() )

# Geliştirmesi devam edenler
- SearchActivity'deki herhangi bir besine tıklandığında, ilgili öğüne ekleme yapılacak
- SearchActivity'de besin arama, ve sayfalama yapılacak
- SearchActivity'deki toolbar'da fotoğraf ikonuna tıklandığında, kamera açılacak ve herhangi bir besinin fotoğrafı çekildiğinde veritabanında ilgili besin varsa, bulunacak
- OgunlerActivity'de tarih seçimi yapıldığında, kullanıcının o gün tükettiği besinler, öğünlerin altına getirilecek
