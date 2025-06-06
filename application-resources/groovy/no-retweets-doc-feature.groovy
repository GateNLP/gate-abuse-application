tweetanns = doc.getAnnotations("Original markups").get("Tweet")
include = "true"
if(tweetanns.size()==1){
 tweetann = tweetanns.iterator().next()
 if(tweetann.getFeatures().get("retweeted_status")!=null){
  include = "false"
 }
} else {
 println("No tweet annotation found in original markups!")
}
doc.getFeatures().put("to-index", include)