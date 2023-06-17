# Ademo
This app was made in a few days. I couldn't find any reference about API, so that's exactly why it boasts of dirty-dirty Html parsing which is working on the word of honor and consistent layouts :)

For example, it could take up to 16 seconds to fetch all pages from https://slushe.com/featured to https://slushe.com/featured/page11.html. Up to 2 seconds per page on a bad day.
I pray the slow connection is an Html parser library issue (Jsoup)
Also, there are some problems with loading animated .webp via the Glide library

This app consists of two:
* Slushe image viewer/browser-esque
* Video player-esque

Currently, there is an ability to log in to the Slushe account (Yet, I'm not using this for anything, other than displaying account details)
Login is done via Android WebView. After that, the app saves PHPSESSID from the cookies and uses it for making requests

The video player is ExoPlayer - the default player implementation in Jetpack Media3 (recommended by Google). It's fairly easily customizable and extendable

#
The source code of the application is inside this repository

Apk file can be downloaded from _**Releases**_ tab
#

## Slushe Viewer

* It gradually fetches (fetch more near the end of the list) items from Slushe main pages(FEATURED/MOST RECENT/MOST LIKES/MOST VIEWED/MOST DISCUSSED LISTS) and displays them in the list
* If a user clicks an image, It fetches details from the item link (It shows only the first image of the bundle)

<p float="left">
  <img src="/.github/1.png" width="200" />
  <img src="/.github/2.png" width="200" />
  <img src="/.github/3.png" width="200" />
</p>

```
      Item in the list
source: https://slushe.com/galleries/cammy-s-anal-training-cumshot-180463.html
image: https://cdn.slushe.com/galleries/5772564837287dcc7a/thumbs/spare30648372a8d3566.webp
title: Cammy's anal training cumshot
author: IhateTwitter
authorImgLink: https://cdn.slushe.com/misc/6339335bb2ada.jpg
```
```
      Details for https://slushe.com/galleries/ada-wong-179306.html
bigImage: https://cdn.slushe.com/galleries/64140647b7ade929bb/main/adawong8647b7b08583ae.jpg
authorImage: https://cdn.slushe.com/misc/6404dd5c4c21b.webp
title: Ada Wong
authorName: Puzz3D
authorPosition: Artist / Commissions
tags: [3d 3dx daz3d hot sexy render Ada Wong]
stats: [959, 23, 37, 0]
```

## Video Player

We need to populate our list with some data - videos (also images, but I haven't implemented it). For the videos source, I use https://slushe.com/HentaiVR posts (they are primarily videos)
```
      HentaiVR posts
https://slushe.com/video/chole-and-kara-testing-179931.html
https://slushe.com/video/harley-midnight-ride-179546.html
https://slushe.com/video/atomic-fuck-179073.html
```
After that, I _casually_ get links for videos sources (m3u8 playlists) from data in the video player scripts:
```
var cloud_vid = '1ff29dea12984ba98718a5fe922950f2';
const source = 'https://videodelivery.net/'+cloud_vid+'/manifest/video.m3u8';
-->
https://videodelivery.net/1ff29dea12984ba98718a5fe922950f2/manifest/video.m3u8
```
Which contain stream URIs. Which can be fed to the ExoPlayer since it supports media streams (hls, dash)

**The first time the video player is used, it gradually fetches (~1-2 minutes) data from the web, and after that, it saves them to the file (20 videos), so it doesn't need to fetch it again afterwards**

In the Content tab, videos can be added to the playlist. In the Playlist tab, they can be removed. Play button starts the video player with data from the playlist

Repeat modes, seek, skip and settings are basic ExoPlayer capabilities

<p float="left">
  <img src="/.github/4.png" width="200" />
  <img src="/.github/5.png" width="200" /> 
  <img src="/.github/6.png" width="200" />
</p>