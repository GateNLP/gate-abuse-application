/* since we've generated multiple Topic annotations for the same string, delete any that have the same span and belong to the same theme
*/

def groups = inputAS.get("Topic").groupBy {
  // a list of whatever criteria you want to group by,
  // here I'm using start and end offsets and the "kind" feature
  [it.start(), it.end(), it.features.theme]
}
groups.each { groupingKey, group ->
  if(group.size() > 1) {
    // [1..-1] is a Groovy idiom for "from index 1 (i.e. the second
    // item) to the end of the list, inclusive". 
    // But tail() does the same thing and looks nicer
    inputAS.removeAll(group.tail())

  }
}
