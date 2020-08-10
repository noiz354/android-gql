# android-gql
android lightweight graphql library

`query_sample.json`
```
mutation employee($name: String, $age: Int, $verified: Boolean) {
    employee(name: $name, age: $age, verified: $verified) {
        status
        data {
            name
            avatar
            phone
            address {
                zip
                province
            }
        }
    }
}
```
`Test.kt`
```kt
Gql()
    .setUrl("https://isfa.test/")
    .query(fileReader("raw/query_sample.json"))
    .parameters(mapOf(
        "name" to "Muh Isfhani Ghiath",
        "age" to 23,
        "verified" to true
    ))
    .requestTo<Model> { response ->
        // response.data
    }
    .error { throwable ->
        // oops!
    }
```
