/* since we've generated multiple Author/RetweetedAuthor annotations for the same string, delete any that have the same span and belong to the same minorType
*/

def groups = inputAS.get("Author").groupBy {
  // a list of whatever criteria you want to group by,
  // here I'm using start and end offsets and the "kind" feature
  [it.start(), it.end(), it.features.minorType]
}
groups.each { groupingKey, group ->
  if(group.size() > 1) {
    // [1..-1] is a Groovy idiom for "from index 1 (i.e. the second
    // item) to the end of the list, inclusive". 
    // But tail() does the same thing and looks nicer
    inputAS.removeAll(group.tail())

  }
}


def retweetedgroups = inputAS.get("RetweetedAuthor").groupBy {
  // a list of whatever criteria you want to group by,
  // here I'm using start and end offsets and the "kind" feature
  [it.start(), it.end(), it.features.minorType]
}
retweetedgroups.each { groupingKey, group ->
  if(group.size() > 1) {
    // [1..-1] is a Groovy idiom for "from index 1 (i.e. the second
    // item) to the end of the list, inclusive". 
    // But tail() does the same thing and looks nicer
    inputAS.removeAll(group.tail())

  }
}


def repliedgroups = inputAS.get("ReplyTo").groupBy {
  // a list of whatever criteria you want to group by,
  // here I'm using start and end offsets and the "kind" feature
  [it.start(), it.end(), it.features.minorType]
}
repliedgroups.each { groupingKey, group ->
  if(group.size() > 1) {
    // [1..-1] is a Groovy idiom for "from index 1 (i.e. the second
    // item) to the end of the list, inclusive". 
    // But tail() does the same thing and looks nicer
    inputAS.removeAll(group.tail())

  }
}
