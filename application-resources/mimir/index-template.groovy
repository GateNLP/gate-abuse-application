import gate.creole.ANNIEConstants
import gate.mimir.SemanticAnnotationHelper.Mode
import gate.mimir.index.OriginalMarkupMetadataHelper
import gate.mimir.db.DBSemanticAnnotationHelper as DefaultHelper
import gate.mimir.util.DocumentFeaturesMetadataHelper
import gate.nesta.LabelKindMentionDescriber
import gate.mimir.util.IgnoreEmptiesTermProcessor

tokenASName = ""
tokenAnnotationType = ANNIEConstants.TOKEN_ANNOTATION_TYPE
tokenFeatures = {
  mimir(directIndex:true)
  string()
  category()
  //root(directIndex:true)
  

  // screen name of tweet author, reply target and original author if this is a retweet, added as a feature on the first token in the tweet
  tweet_author(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  in_reply_to(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  retweeted_politician(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // Twitter IDs of tweet author, reply target and original author if this is a retweet, added as a feature on the first token in the tweet
  tweet_author_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  in_reply_to_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  retweeted_politician_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // ID of the specific tweet that this is a reply to
  in_reply_to_status_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // ID of the specific tweet that this is a retweet of
  retweet_of_status_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // ID of this tweet
  tweet_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // User ID on @mentions
  user_id(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // full string of hashtag, added to the Token annotation on the #
  hashtag_string(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // Topic theme, subtheme and description "root (subtheme)", added to first token of Topic
  topic_theme(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  topic_subtheme(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  topic_description(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)

  // abusive and offensive words
  abuse_string(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
  offensive_string(directIndex:true, termProcessor:IgnoreEmptiesTermProcessor.INSTANCE)
}

semanticASName = ""
semanticAnnotations = {
  index {
    // document features
    annotation helper:new DefaultHelper(annType:'DocumentKind', mode:Mode.DOCUMENT,
        nominalFeatures:["tweet_kind"])
    annotation helper:new DefaultHelper(annType:'DocumentTimestamp', mode:Mode.DOCUMENT,
        integerFeatures:["hour_timestamp"])
    annotation helper:new DefaultHelper(annType:'DocumentAbuseLevel', mode:Mode.DOCUMENT,
        integerFeatures:["abuse_count"])
    annotation helper:new DefaultHelper(annType:'DocumentVeracity', mode:Mode.DOCUMENT,
        floatFeatures:["veracity_score", "veracity_true", "veracity_false", "veracity_unverified"])

  }
  index(directIndex:true) {
    annotation helper:new DefaultHelper(annType:'Author',
        nominalFeatures:["party", "gender", "ethnicity", "orientation", "minorType"],
        textFeatures:["politician_name", "politician_handle", "constituency", "twitter_id"],
        mentionDescriber:new LabelKindMentionDescriber("politician_handle", "party"))
    annotation helper:new DefaultHelper(annType:'ReplyTo',
        nominalFeatures:["party", "gender", "ethnicity", "orientation", "minorType"],
        textFeatures:["name", "twitter_handle", "constituency", "twitter_id"],
        mentionDescriber:new LabelKindMentionDescriber("twitter_handle", "party"))
    annotation helper:new DefaultHelper(annType:'RetweetedAuthor',
        nominalFeatures:["party", "gender", "ethnicity", "orientation", "minorType"],
        textFeatures:["name", "twitter_handle", "constituency", "twitter_id"],
        mentionDescriber:new LabelKindMentionDescriber("twitter_handle", "party"))
  }
  index(directIndex:true) {
    annotation helper:new DefaultHelper(annType:'Politician', 
        nominalFeatures:["party", "gender", "ethnicity", "orientation", "minorType"],
        textFeatures:['constituency', 'name', 'twitter_handle', 'twitter_id'])
    annotation helper:new DefaultHelper(annType:'Topic', nominalFeatures:["theme"],
        mentionDescriber:new LabelKindMentionDescriber("theme", null))

    annotation helper:new DefaultHelper(annType:'Abuse',
        nominalFeatures:['minorType', 'type', 'strength', 'community', 'target', 'confidence', 'subtype'],
        integerFeatures:['abusiveTermCount'])

    annotation helper:new DefaultHelper(annType:'OffensiveLookup',
        nominalFeatures:['type', 'category', 'strength'])
  }
}
documentRenderer = new OriginalMarkupMetadataHelper()

documentFeaturesHelper = new DocumentFeaturesMetadataHelper("author", "timestamp", "retweet_of_screen_name", "veracity_score", "veracity_true", "veracity_false", "veracity_unverified")
documentMetadataHelpers = [documentRenderer, documentFeaturesHelper]

// miscellaneous options
timeBetweenBatches = 15.minutes
//reduce maximumBatches to try and rein in too many open files errors
maximumBatches = 8
