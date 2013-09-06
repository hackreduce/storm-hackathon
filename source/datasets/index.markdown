---
layout: page
title: "Data sets and APIs"
date: 2013-07-09 10:40
comments: true
sharing: true
footer: true
---

Though you are welcome to use any dataset you'd like, we've made it a bit easier to use a few.

# Twitter's Gardenhose

We've sucked in the past 3 days worth of a random sampling of all public tweets which is approximately 4M tweets. This stream is still active, so it will suck in tweets as they happen during the hackathon.

To use this dataset, take a look at the example topology [here](https://github.com/hackreduce/storm-hackathon/tree/master/src/main/java/org/hackreduce/storm/twitter).

# Gnip

[Gnip](http://gnip.com) is the world's largest and most trusted provider of social media data, serving customers in a range of industries. Gnip's customers deliver social media analytics to over 90% of the Fortune 500. Gnip delivers more than 100 billion realtime social data activities each month, providing access to data from dozens of sources including Twitter, Facebook, etc. 

*  Gnip enterprise data collector supported sites
    *  bitly
    *  Delicious
    *  Flickr
    *  Google+
    *  Instagram
    *  Reddit
    *  StackOverflow
    *  Vimeo
    *  Youtube
    *  etc.
*   [Full list of feeds available](http://support.gnip.com/customer/portal/articles/499176-feed-info)
*   [Details working with Gnip](/gnip/)
*   [Example Gnip Project](https://github.com/hackreduce/storm-hackathon/tree/master/src/main/java/org/hackreduce/storm/gnip)

# Semantria

Implement text/sentiment analysis into your project using [Semantria's](semantria.com/register) REST API. Simply register at http://semantria.com/register and let them know if you will be processing more than 10,000 documents.

*   All hackers will have unlimited transactions during the Hackathon (1 transaction to process 1 document), and will receive a free 100k transactions after the Hackathon.
*   For SDK’s & additional information, check out the developer page at http://semantria.com/developer
*   For support please use our live chat on our website, our CTO/head developer will be answering all technical questions.

# Static data sets

We preloaded 2 static datasets from our Map/Reduce hacktahons:

*   Wikipedia: a wikipedia dump (XML format). See [here](https://github.com/hackreduce/storm-hackathon/tree/master/src/main/java/org/hackreduce/storm/example/wikipedia) for an example
*   NYSE: daily highs of companies traded on the NYSE between 1970 and 2010. See [here](https://github.com/hackreduce/storm-hackathon/tree/master/src/main/java/org/hackreduce/storm/example/stock) for an example usage.

Other static datasets are available and can be loaded on demand (ask one of the mentors):

*   [hack/reduce dataset collection](https://github.com/hackreduce/Hackathon/tree/master/datasets)

# Real-time APIs

*   [Real-time API Directory](http://blog.programmableweb.com/2012/04/17/62-real-time-apis-twitter-thrutu-and-pusher/)

# Transit
*   [Google Transit](https://developers.google.com/transit/community)
*   [Google Transit Data Feeds](http://code.google.com/p/googletransitdatafeed/wiki/PublicFeeds)
*   [MBTA](http://www.mbta.com/rider_tools/developers/default.asp?id=22393)

# Financial
*   [Yahoo Finance Feeds](http://finance.yahoo.com/news/rssindex/)

# Miscellaneous
*   [Craigslist Data Feeds](http://www.craigslist.org/about/rss)
*   [USGS Earthquake Feed](http://earthquake.usgs.gov/earthquakes/feed/v1.0/)