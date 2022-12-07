package com.lxn.gdghanoicheckin.constant

object Constant {
    //    https://script.google.com/macros/s/AKfycbzofqzqkYYh85gZBXqT7nmhXBhEBAtGs8tD0jcAq3GCMDwcsqV6jgCXTI35olARQmMoug/exec
    ///https://script.google.com/macros/s/AKfycbzVrNgkLUHSAdjoo46MQ16djDwLUb48YCWmRReOzvvm60zH5EX_ZAlaE4t6CPZCXxCrvw/exec
    // https://script.google.com/macros/s/AKfycbzZPmgB0dzrZPzDMxNGW9VJPQfv6zYmVyoBo94LwO-O3QbLVshidwON7tnUHZaKCfBD6A/exec

    const val BASE_URL = "https://script.google.com/macros/s/AKfycbyfJZWHznGSxQ4j1qreEjf-8wqvAg-jFVzDGV9icwD1tRAC72L6FGZs8opKZt3UAxydXQ/"
    const val LOADER_EXPIRED_TIME = 60
}

enum class TypeCheckIn(val value: String) {
    Normal(value = "normal"),
    Vip(value = "vip"),
    IsExited(value = "IsExited")
}