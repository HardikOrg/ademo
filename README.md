# Ademo

> First of all: I don't know how GitHub will tolerate NSFW content in this repository. I have wrapped all screenshots with spoilers (details tag); hopefully, it would chill them a bit.

* This app is a couple of days of work + some afterward commits
* I couldn't find any reference to API, that's exactly why this app boasts of dirty-dirty Html parsing which is working on the word of honor and consistent layouts :)

HTML fetching times are extremely inconsistent, it could take up to 4 seconds to fetch a page, don't ask me why. Hey, but it only happens on bad days with an angry server 😉 → On a **very** bad day it could take up to 30-40 seconds to fetch 20 pages of images(20 is the number of images on the page - https://slushe.com/featured, https://slushe.com/featured/page2.html, etc). Generally, it is faster to fetch a page full of images than a page with a video

## Techincal stuff

This app consists of two:
* Slushe image viewer/browser-esque
* Video player-esque

Also, currently, there is an ability to log in to the Slushe account (I'm not using this for fetching or showing any posts or user-specific uploads; only for displaying account details).
Login is done via Android WebView. After that, the app saves PHPSESSID from the cookies and makes it possible to do requests as a logged-in user. I think. Only if it's not more complicated

The video player is ExoPlayer - the default player implementation in Jetpack Media3 (recommended by Google). It's fairly easily customizable and extendable

I use **Jsoup** for HTML parsing and fetching + **okhttp3** for building HTTP requests. **Glide** for image loading (and caching). For some reason, Glide doesn't support .webp → Some images from Slushe main pages could be shown incorrectly

> The full source code of the application is inside this repository. Apk file can also be downloaded from the _**Releases**_ tab

[![stable release](https://img.shields.io/github/release/HardikOrg/ademo.svg?maxAge=3600&label=download)](https://github.com/HardikOrg/ademo/releases/latest)

## Slushe Viewer

* It gradually fetches (fetch more near the end of the list) items from Slushe main pages(FEATURED/MOST RECENT/MOST LIKES/MOST VIEWED/MOST DISCUSSED LISTS) and displays them in the list
* If a user clicks an image, It fetches details from the item link (If there is a bundle, it shows only the first image)

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
tags: [3d, 3dx, daz3d, hot, sexy, render, Ada Wong]
stats: [959, 23, 37, 0]
```

<details>
  <summary><b>Screenshots #1</b></summary>
  <p float="left">
    <img src="/.github/1.png" width="200" />
    <img src="/.github/2.png" width="200" />
    <img src="/.github/3.png" width="200" />
  </p>
</details>

## Video Player

This is kinda an example of a player from the Software Requirements:
* Video player (play, pause, stop, etc.)
  > Yeah
* Ability to play video clips and images in sequence
  > There is one without images in sequence
* Ability to loop individual video clips and sets of video clips
  > Yeah
* Ability to create a sequence of video clips within sections that work independently from each other
  > Eh?
* Ability to create custom playlists
  > Yeah

To start working with a player we need some content. I am using the latest videos from the user https://slushe.com/HentaiVR

```
      HentaiVR posts
https://slushe.com/video/chole-and-kara-testing-179931.html
https://slushe.com/video/harley-midnight-ride-179546.html
https://slushe.com/video/atomic-fuck-179073.html
```
After that, I _casually_ get links for videos sources (m3u8 playlists) from the data in the video player scripts:
```
var cloud_vid = '1ff29dea12984ba98718a5fe922950f2';
const source = 'https://videodelivery.net/'+cloud_vid+'/manifest/video.m3u8';
→
https://videodelivery.net/1ff29dea12984ba98718a5fe922950f2/manifest/video.m3u8
```

* The second step needs to get a link to m3u8 (which can be fed to the ExoPlayer since it supports media streams [hls, dash]) and an image preview
* This process is rather slow. That's why:
  * There is some prefetched videos' data saved - 20 items
  * If for some reason prefetched data did not work. The app will fetch 20 fresh new ones (1-2 minutes) and gradually display them as they arrive
  * Everything that was fetched will be saved in the file to prevent refetching afterward

<details>
  <summary><b>Example of fetching timings</b></summary>

  ```
  Get Videos: 2.073089300s    -- First step
     fetch doc: 5.181666s
     parse doc: 7.717800ms
  Video took: 5.189639200s    -- 1 video
     fetch doc: 3.680549200s
     parse doc: 812.9us
  Video took: 3.681573600s    -- 2 video
     fetch doc: 2.685169900s
     parse doc: 754.3us
  Video took: 2.686144600s    -- 3 video
     fetch doc: 2.931910700s
     parse doc: 719.7us
  Video took: 2.932850s       -- 4 video
  ```
</details>

In the Content tab, videos can be added to the playlist. In the Playlist tab, they can be removed. The play button starts the video player with data from the playlist. You can only play videos from the playlist. Repeat modes, seek, skip and settings are basic ExoPlayer capabilities

<details>
  <summary><b>Screenshots #2</b></summary>
  <p float="left">
    <img src="/.github/4.png" width="200" />
    <img src="/.github/5.png" width="200" /> 
    <img src="/.github/6.png" width="200" />
  </p>
</details>

## Shameful corner

* Yes, I do not use different classes for fetched data and displayed data (DTOs and "responses")
* Yes, I do not use any Dependency Injection, I do not need such an excessive testing capability in this demo
* Yes, the number of interfaces is pathetic
* Yes, I am using a Repository "keyword" but not a repository pattern (but kinda am). `Room`? Whom? Glide cache does the trick
* Yes, it is `notifyDataSetChanged()` everywhere. No, there is no DiffUtil
* Yes, it is meh on the styles and themes front. Yes, it is a necessity to make proper styles
* Yes, there are some tests - some non-android related functions
* And yes, it is a fat-ass code; sue me