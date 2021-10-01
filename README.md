
<h1 align="center">🎨 Gabriela's Image Generator 🎨</h1>
<img height="250" src="https://cdn.discordapp.com/attachments/696865625259114626/771103144553087006/1603915637147.png" align="right">

**🚧 Experiemental Project 🚧**

A image and video generation microservice image and video generation and API, used in [Loritta](https://github.com/LorittaBot/Loritta).

## 🤔 Why?

Originally the service was created to generate "video" memes for Loritta's `+carlyaaah` command, however it grew to generate "static" and "normal" kinds of memes.

There are also other reasons, like:

1. Image generation is stateless, so we are able to create multiple instances of the generator and scale out in multiple machines.
2. Allows other services to implement and communicate with the image generation server.
3. By splitting up the image generation from Loritta to a separate project, we can improve the service without depending on Loritta updates. Also means that a bad image won't crash Loritta, just the image generation server.
4. Because why not?

## 😊 Yay?

Yay!