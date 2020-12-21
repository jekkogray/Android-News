package com.example.androidnews

import java.io.Serializable

data class News(val title: String, val thumbnailUrl: String, val source: String, val url: String, val content: String):Serializable

/**
 * source": {
"id": null,
"name": "Space.com"
},
"author": "Chelsea Gohd",
"title": "Antares rocket launches new astronaut toilet and more to space station for NASA - Space.com",
"description": "A brand-new space toilet has reached the final frontier!",
"url": "https://www.space.com/nasa-space-toilet-launches-on-antares-cygnus-ng-14",
"urlToImage": "https://cdn.mos.cms.futurecdn.net/PqVCmpWA83WgDCTSthbdF4-1200-80.jpg",
"publishedAt": "2020-10-03T01:53:00Z",
"content": "A robotic Cygnus spacecraft successfully blasted off from Virginia late Friday (Oct. 2) carrying nearly 4 tons of gear, including a new space toilet, to the International Space Station. \r\nA Northrop … [+5228 chars]"
 * */