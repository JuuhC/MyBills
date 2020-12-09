package com.carrati.domain.models

class Response(val status: Status,
               val data: Any?,
               val error: Throwable?) {


    companion object {
        fun loading(): Response {
            return Response(Status.LOADING, null, null)
        }

        fun <T: Any>success(data: T): Response {
            return Response(Status.SUCCESS, data, null)
        }

        fun error(error: Throwable): Response {
            return Response(Status.ERROR, null, error)
        }

        fun empty(): Response {
            return Response(Status.EMPTY_RESPONSE, null, null)
        }
    }

    enum class Status {
        LOADING,
        SUCCESS,
        EMPTY_RESPONSE,
        ERROR
    }
}