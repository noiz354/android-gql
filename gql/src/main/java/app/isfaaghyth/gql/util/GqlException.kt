package app.isfaaghyth.gql.util

import app.isfaaghyth.gql.Gql

class GqlException(message: String): Gql() {
    init { throw Exception(message) }
}