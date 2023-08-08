package com.example.userswithposts.models

import kotlinx.serialization.Serializable

// Commented lines must be uncommented if ignoreUnknownKeys in Json is false.
@Serializable
data class User (
    val id: Int,
    val firstName: String?,
    val lastName: String?,
    val maidenName: String?,
    val age: Int,
    val gender: String,
    val email: String?,
    val phone: String?,
    val username: String?,
//    val password: String?,
    val birthDate: String?,
    val image: String?,
//    val bloodGroup: String?,
//    val height: Double?,
//    val weight: Double?,
//    val eyeColor: String,
//    val hair: Hair?,
//    val domain: String?,
//    val ip: String?,
    val address: Address?,
//    val macAddress: String?,
//    val university: String?,
//    val bank: Bank?,
//    val company: Company?,
//    val ein: String?,
//    val ssn: String?,
//    val userAgent: String?
)

/*
{
Example:

  "id": 1,
  "firstName": "Terry",
  "lastName": "Medhurst",
  "maidenName": "Smitham",
  "age": 50,
  "gender": "male",
  "email": "atuny0@sohu.com",
  "phone": "+63 791 675 8914",
  "username": "atuny0",
  "password": "9uQFF1Lh",
  "birthDate": "2000-12-25",
  "image": "https://robohash.org/hicveldicta.png?size=50x50&set=set1",
  "bloodGroup": "A−",
  "height": 189,
  "weight": 75.4,
  "eyeColor": "Green",
  "hair": {
    "color": "Black",
    "type": "Strands"
  },
  "domain": "slashdot.org",
  "ip": "117.29.86.254",
  "address": {
    "address": "1745 T Street Southeast",
    "city": "Washington",
    "coordinates": {
      "lat": 38.867033,
      "lng": -76.979235
    },
    "postalCode": "20020",
    "state": "DC"
  },
  "macAddress": "13:69:BA:56:A3:74",
  "university": "Capitol University",
  "bank": {
    "cardExpire": "06/22",
    "cardNumber": "50380955204220685",
    "cardType": "maestro",
    "currency": "Peso",
    "iban": "NO17 0695 2754 967"
  },
  "company": {
    "address": {
      "address": "629 Debbie Drive",
      "city": "Nashville",
      "coordinates": {
        "lat": 36.208114,
        "lng": -86.58621199999999
      },
      "postalCode": "37076",
      "state": "TN"
    },
    "department": "Marketing",
    "name": "Blanda-O'Keefe",
    "title": "Help Desk Operator"
  },
  "ein": "20-9487066",
  "ssn": "661-64-2976",
  "userAgent": "Mozilla/5.0 ..."
}
 */