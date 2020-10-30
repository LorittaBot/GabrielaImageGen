
<h1 align="center">🎨 Gabriela's Image Generator 🎨</h1>
<img height="250" src="https://cdn.discordapp.com/attachments/696865625259114626/771103144553087006/1603915637147.png" align="right">

**🚧 Experiemental Project 🚧**

A image and video generation microservice + multiplatform (JVM/JS) image generation and API, used in [Loritta](https://github.com/LorittaBot/Loritta) and https://memes.lori.fun/.

## 📁 Project Structure

*  📜 **Gabriela's Image API** [`gabriela-image-api`]
> Gabriela's Image API, Multiplatform (Graphics2D in Java, Canvas in Browser/Node)

*  🎨 **Image Generators** [`image-generators`]
> Module with a bunch of multiplatform image generators + assets.

*  🔗 **Image Generator (Microservice)** [`image-generator-server`]
> A microservice (web) that has REST endpoints, allowing other services to generate nice and funny images or videos.

*  🐱‍💻 **Image Generator (Browser)** [`image-generator-browser`]:
> Why generate "haha funny memes" in the server side if you can generate them on the client side?  

## 🤔 Why?

Originally the service was created to generate "video" memes for Loritta's `+carlyaaah` command, however it grew to generate "static" and "normal" kinds of memes.

There are also other reasons, like:

1. Making the Image API multiplatform, allowing the memes to be created on the browser via JavaScript: https://memes.lori.fun/
2. Image generation is stateless, so we are able to create multiple instances of the generator and scale out in multiple machines.
3. Allows other services to implement and communicate with the image generation server.
4. By splitting up the image generation from Loritta to a separate project, we can improve the service without depending on Loritta updates. Also means that a bad image won't crash Loritta, just the image generation server.
5. Because why not?

## 👷 Using the API

### Using the `image-generators` module

```kotlin
// Your source image
val sourceImage = JVMImage(ImageIO.read(...))

// Creates the Canella DVD Generator with the "/canella_dvd/template.png" template
// The file is stored in the "image-generators" JAR resources
val generator = CanellaDVDGenerator(
	JVMImage(
		ImageIO.read(
CanellaDVDGenerator::class.java.getResourceAsStream("/canella_dvd/template.png")
		)
	)
)  

// Generates a edited image with your sourceImage
val finalImage = generator.generate(sourceImage)

// Now you can write it to a file!
File("result.png").writeBytes(finalImage.toByteArray(Image.FormatType.PNG))
```

![https://cdn.discordapp.com/attachments/513405772911345664/771819056150872124/canella_dvd.png](https://cdn.discordapp.com/attachments/513405772911345664/771819056150872124/canella_dvd.png)

So cool!

### Using the `image-generator-server` REST API

Soon

## 😊 Yay?

Yay!