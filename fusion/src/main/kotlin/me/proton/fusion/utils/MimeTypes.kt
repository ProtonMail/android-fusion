package me.proton.fusion.utils

/**
 * Contains multiple MIME types to test intents.
 */
object MimeTypes {

    val application = Application
    val text = Text
    val image = Image
    val video = Video

    object Application {
        const val pdf = "application/pdf"
        const val zip = "application/zip"
        const val docx = "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    }

    object Text {
        const val plain = "text/plain"
        const val rtf = "text/rtf"
        const val html = "text/html"
        const val json = "text/json"
    }

    object Image {
        const val png = "image/png"
        const val jpeg = "image/jpeg"
        const val gif = "image/gif"
    }

    object Video {
        const val mp4 = "video/mp4"
        const val jp3 = "video/3gp"
    }
}
