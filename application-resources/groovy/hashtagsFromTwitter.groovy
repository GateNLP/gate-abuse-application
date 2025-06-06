// Replace detected hashtags with those supplied by Twitter as "entities", to catch tags that are truncated at the end of a retweet

def hashtagsFromTwitter = doc.getAnnotations('Original markups')["hashtags"]
if(hashtagsFromTwitter) {
  inputAS.removeAll(inputAS["Hashtag"])
  hashtagsFromTwitter.each { ht ->
    outputAS.addAnn(ht, "Hashtag", Utils.featureMap(
          "length", ht.lengthLong(),
          "kind", "Hashtag",
          "rule", "FromTwitter",
          "string", "#" + ht.features.text)) // text from twitter is the full un-truncated version
  }
}
