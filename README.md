# SHCFinance
SHCFinance is an Android-based application that provides digital financial management services to record income, expenditure, and accounts payable transactions in more detail without using paper and pen, inspired by [BukuKas](https://bukukas.co.id/). This personal project is intended for the Android Based Application Programming course final exam at Petra Christian University.

### Project report 💸 [Click here!](https://drive.google.com/file/d/1RCGe4YCF0rGKDeZ3W8NVHCkP7iLE3TeO/view?usp=sharing)

## Framework, Language, & Toolkits
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=for-the-badge&logo=android-studio&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Firebase](https://img.shields.io/badge/firebase-ffca28?style=for-the-badge&logo=firebase&logoColor=black)
![Material Design](https://img.shields.io/badge/material%20design-757575?style=for-the-badge&logo=material%20design&logoColor=white)

## Database Structure
```kotlin
data class transaction(
    var id : String, // UUID transaksi
    var description : String, // catatan/keterangan transaksi
    var sum_of_money : Int, // jumlah uang transaksi
    var type : String, // tipe transaksi (income atau outcome)
    var id_category : String, // identifier dari jenis kategori
    var date : String // tanggal transaksi berlangsung
)

data class category(
    var id : String, // UUID jenis kategori
    var name : String // nama jenis kategori
)
```

## Preview

### Splash Screen
<img src="https://user-images.githubusercontent.com/56993480/156015838-b183635b-a741-43a9-9a31-ec76e2a8eafc.png" width="272" />

### Home Activity
Users can filter by date to see a list of transactions on a specific date using the [DatePicker](https://developer.android.com/reference/kotlin/android/widget/DatePicker) widget.
<p float="left">
  <img src="https://user-images.githubusercontent.com/56993480/156015886-370dbd01-c362-4791-a30e-1de76cdc4ed7.png" width="272" />
  <img src="https://user-images.githubusercontent.com/56993480/156015907-a2a11f33-75ee-452e-bfb5-3f8af1f07c0d.png" width="272" /> 
  <img src="https://user-images.githubusercontent.com/56993480/156015930-bc7bbba7-0ab5-4dc0-a13e-d91462dd4620.png" width="272" />
</p>

### Add New Transaction Activity
<p float="left">
  <img src="https://user-images.githubusercontent.com/56993480/156016163-bb2b4312-3546-48bc-bf93-5778f1d3b0ca.png" width="272" />
  <img src="https://user-images.githubusercontent.com/56993480/156016180-580b25c5-c6f3-49dd-8fe1-8af10b9988a1.png" width="272" /> 
</p>
<p float="left">
  <img src="https://user-images.githubusercontent.com/56993480/156017890-76131326-2393-4ade-8107-f79e56434bb7.png" width="272" />
  <img src="https://user-images.githubusercontent.com/56993480/156017903-caf690d2-127e-4be4-812c-2173ed64e94b.png" width="272" /> 
</p>

### Error Checking
<p float="left">
  <img src="https://user-images.githubusercontent.com/56993480/156017618-c812b525-eb1a-424c-9dd2-1f6862760f1a.png" width="272" />
  <img src="https://user-images.githubusercontent.com/56993480/156017632-2cf76dbc-07bc-4128-82e3-b204c0bd8564.png" width="272" /> 
</p>

### Add New Category Dialog Box
<p float="left">
  <img src="https://user-images.githubusercontent.com/56993480/156016806-8f1933dc-92cb-4384-8439-be93d7493017.png" width="272" />
  <img src="https://user-images.githubusercontent.com/56993480/156016760-085e33be-ddbb-4208-94bd-3b13bd530336.png" width="272" /> 
</p>
