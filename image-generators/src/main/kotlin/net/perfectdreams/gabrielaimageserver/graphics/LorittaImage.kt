package net.perfectdreams.gabrielaimageserver.graphics

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import java.awt.*
import java.awt.color.ColorSpace
import java.awt.geom.AffineTransform
import java.awt.image.*
import java.io.*
import java.util.*
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import javax.imageio.ImageWriteParam
import javax.imageio.metadata.IIOMetadata
import javax.imageio.metadata.IIOMetadataNode
import javax.imageio.plugins.jpeg.JPEGImageWriteParam

/*
 * Isto é a mesma classe do javaxt "Image", mas com uma correção no setCorners para não mover a imagem para o canto da tela caso seja transparente
 */
//Imports for JPEG
//import com.sun.image.codec.jpeg.*; //<-- Not always available in newer versions

//Imports for JP2
//import javax.media.jai.RenderedOp;
//import com.sun.media.imageio.plugins.jpeg2000.J2KImageReadParam;


//******************************************************************************
//**  Image Utilities - By Peter Borissow
//******************************************************************************
/**
 * Used to open, resize, rotate, crop and save images.
 *
 */

class LorittaImage {

    //**************************************************************************
    //** getBufferedImage
    //**************************************************************************
    /**  Returns the java.awt.image.BufferedImage represented by the current
     * image.
     */
    lateinit var bufferedImage: BufferedImage
        private set
    private var corners: java.util.ArrayList<Float>? = null

    private var outputQuality = 1f //0.9f; //0.5f;


    private var g2d: Graphics2D? = null


    private var metadata: IIOMetadata? = null
    private var exif: HashMap<Int, Any>? = null
    private var iptc: HashMap<Int, Any>? = null
    private var gps: HashMap<Int, Any>? = null


    //**************************************************************************
    //** getInputFormats
    //**************************************************************************
    /**  Used to retrieve a list of supported input (read) formats.  */

    val inputFormats: Array<String>
        get() = getFormats(ImageIO.getReaderFormatNames())


    //**************************************************************************
    //** getOutputFormats
    //**************************************************************************
    /**  Used to retrieve a list of supported output (write) formats.  */

    val outputFormats: Array<String>
        get() = getFormats(ImageIO.getWriterFormatNames())


    //**************************************************************************
    //** getWidth
    //**************************************************************************
    /**  Returns the width of the image, in pixels.  */

    //**************************************************************************
    //** setWidth
    //**************************************************************************
    /**  Resizes the image to a given width. The original aspect ratio is
     * maintained.
     */
    var width: Int
        get() = bufferedImage.width
        set(Width) {
            val ratio = Width.toDouble() / this.width.toDouble()

            val dw = this.width * ratio
            val dh = this.height * ratio

            val outputWidth = Math.round(dw).toInt()
            val outputHeight = Math.round(dh).toInt()

            resize(outputWidth, outputHeight)
        }


    //**************************************************************************
    //** getHeight
    //**************************************************************************
    /**  Returns the height of the image, in pixels.  */

    //**************************************************************************
    //** setHeight
    //**************************************************************************
    /**  Resizes the image to a given height. The original aspect ratio is
     * maintained.
     */
    var height: Int
        get() = bufferedImage.height
        set(Height) {
            val ratio = Height.toDouble() / this.height.toDouble()

            val dw = this.width * ratio
            val dh = this.height * ratio

            val outputWidth = Math.round(dw).toInt()
            val outputHeight = Math.round(dh).toInt()

            resize(outputWidth, outputHeight)
        }


    //**************************************************************************
    //** getGraphics
    //**************************************************************************

    private//Enable anti-alias
    val graphics: Graphics2D
        get() {
            if (g2d == null) {
                g2d = this.bufferedImage.createGraphics()
                g2d!!.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON)
            }
            return g2d!!
        }


    //**************************************************************************
    //** getHistogram
    //**************************************************************************
    /** Returns an array with 4 histograms: red, green, blue, and average
     * <pre>
     * ArrayList<int></int>[]> histogram = image.getHistogram();
     * int[] red = histogram.get(0);
     * int[] green = histogram.get(1);
     * int[] blue = histogram.get(2);
     * int[] average = histogram.get(3);
    </pre> *
     */
    //Create empty histograms
    //Populate the histograms
    val histogram: java.util.ArrayList<IntArray>
        get() {
            val red = IntArray(256)
            val green = IntArray(256)
            val blue = IntArray(256)
            val average = IntArray(256)

            for (i in red.indices) red[i] = 0
            for (i in green.indices) green[i] = 0
            for (i in blue.indices) blue[i] = 0
            for (i in average.indices) average[i] = 0
            for (i in 0 until this.width) {
                for (j in 0 until this.height) {
                    val color = this.getColor(i, j)
                    val r = color.red
                    val g = color.green
                    val b = color.blue

                    red[r] = red[r] + 1
                    green[g] = green[g] + 1
                    blue[b] = blue[r] + 1

                    val avg = Math.round(((r + g + b) / 3).toFloat())
                    average[avg] = average[avg] + 1
                }
            }

            val hist = java.util.ArrayList<IntArray>()
            hist.add(red)
            hist.add(green)
            hist.add(blue)
            hist.add(average)
            return hist
        }


    //**************************************************************************
    //** getImage
    //**************************************************************************
    /**  Returns a java.awt.Image copy of the current image.  */

    val image: java.awt.Image?
        get() = bufferedImage

    //**************************************************************************
    //** getImage
    //**************************************************************************
    /**  Returns a java.awt.image.RenderedImage copy of the current image.  */

    val renderedImage: java.awt.image.RenderedImage?
        get() = bufferedImage


    //**************************************************************************
    //** getByteArray
    //**************************************************************************
    /** Returns the image as a jpeg byte array. Output quality is set using
     * the setOutputQuality method.
     */
    val byteArray: ByteArray?
        get() = getByteArray("jpeg")


    //**************************************************************************
    //** getImageType
    //**************************************************************************

    private val imageType: Int
        get() = getImageType(this.bufferedImage)


    //**************************************************************************
    //** getIIOMetadata
    //**************************************************************************
    /** Returns the raw, javax.imageio.metadata.IIOMetadata associated with this
     * image. You can iterate through the metadata using an xml parser like this:
     * <pre>
     * IIOMetadata metadata = image.getMetadata().getIIOMetadata();
     * for (String name : metadata.getMetadataFormatNames()) {
     * System.out.println( "Format name: " + name );
     * org.w3c.dom.Node metadataNode = metadata.getAsTree(name);
     * System.out.println(javaxt.xml.DOM.getNodeValue(metadataNode));
     * }
    </pre> *
     */
    //**************************************************************************
    //** setIIOMetadata
    //**************************************************************************
    /** Used to set/update the raw javax.imageio.metadata.IIOMetadata associated
     * with this image.
     */
    var iioMetadata: IIOMetadata?
        get() = metadata
        set(metadata) {
            this.metadata = metadata
            iptc = null
            exif = null
            gps = null
        }


    //**************************************************************************
    //** getIptcData
    //**************************************************************************
    /** Returns the raw IPTC byte array (marker 0xED).
     */
    val iptcData: ByteArray
        get() = getUnknownTags(0xED)[0].userObject as ByteArray


    //**************************************************************************
    //** getIptcTags
    //**************************************************************************
    /** Used to parse IPTC metadata and return a list of key/value pairs found
     * in the metadata. You can retrieve specific IPTC metadata values like
     * this:
     * <pre>
     * LorittaImage image = new LorittaImage("/temp/image.jpg");
     * java.util.HashMap&lt;Integer, String&gt; iptc = image.getIptcTags();
     * System.out.println("Date: " + iptc.get(0x0237));
     * System.out.println("Caption: " + iptc.get(0x0278));
     * System.out.println("Copyright: " + iptc.get(0x0274));
    </pre> *
     */
    val iptcTags: HashMap<Int, Any>
        get() {

            if (iptc == null) {
                iptc = HashMap()
                for (marker in getUnknownTags(0xED)) {
                    val iptcData = marker.userObject as ByteArray
                    val tags = MetadataParser(iptcData, 0xED).getTags("IPTC")
                    iptc!!.putAll(tags!!)
                }
            }
            return iptc!!
        }


    //**************************************************************************
    //** getExifData
    //**************************************************************************
    /** Returns the raw EXIF byte array (marker 0xE1).
     */
    val exifData: ByteArray
        get() = getUnknownTags(0xE1)[0].userObject as ByteArray


    //**************************************************************************
    //** getExifTags
    //**************************************************************************
    /** Used to parse EXIF metadata and return a list of key/value pairs found
     * in the metadata. Values can be Strings, Integers, or raw Byte Arrays.
     * You can retrieve specific EXIF metadata values like this:
     * <pre>
     * LorittaImage image = new LorittaImage("/temp/image.jpg");
     * java.util.HashMap&lt;Integer, Object&gt; exif = image.getExifTags();
     * System.out.println("Date: " + exif.get(0x0132));
     * System.out.println("Camera: " + exif.get(0x0110));
     * System.out.println("Focal Length: " + exif.get(0x920A));
     * System.out.println("F-Stop: " + exif.get(0x829D));
     * System.out.println("Shutter Speed: " + exif.get(0x829A));
    </pre> *
     * Note that the EXIF MakerNote is not parsed.
     */
    val exifTags: HashMap<Int, Any>?
        get() {
            if (exif == null) parseExif()
            return exif
        }


    //**************************************************************************
    //** getGpsTags
    //**************************************************************************
    /** Used to parse EXIF metadata and return a list of key/value pairs
     * associated with GPS metadata. Values can be Strings, Integers, or raw
     * Byte Arrays.
     */
    val gpsTags: HashMap<Int, Any>?
        get() {
            if (gps == null) parseExif()
            return gps
        }


    //**************************************************************************
    //** getGPSCoordinate
    //**************************************************************************
    /** Returns the x/y (lon/lat) coordinate tuple for the image. Value is
     * derived from EXIF GPS metadata (tags 0x0001, 0x0002, 0x0003, 0x0004).
     */
    //N
    //W
    val gpsCoordinate: DoubleArray?
        get() {
            exifTags
            try {
                var lat: Double = getCoordinate(gps!![0x0002] as String)
                var lon: Double = getCoordinate(gps!![0x0004] as String)
                val latRef = gps!![0x0001] as String
                val lonRef = gps!![0x0003] as String

                if (!latRef.equals("N", ignoreCase = true)) lat = (-lat)
                if (!lonRef.equals("E", ignoreCase = true)) lon = (-lon)

                return doubleArrayOf(lon, lat)
            } catch (e: Exception) {
                return null
            }

        }


    //**************************************************************************
    //** getGPSDatum
    //**************************************************************************
    /** Returns the datum associated with the GPS coordinate. Value is
     * derived from EXIF GPS metadata (tag 0x0012).
     */
    val gpsDatum: String
        get() {
            exifTags
            return gps!![0x0012] as String
        }


    //**************************************************************************
    //** Constructor
    //**************************************************************************
    /**  Creates a new instance of this class using an existing image  */

    constructor(PathToImageFile: String) : this(java.io.File(PathToImageFile))

    constructor(file: java.io.File) {
        try {
            createBufferedImage(FileInputStream(file))
        } catch (e: Exception) {
        }

    }

    constructor(InputStream: java.io.InputStream) {
        createBufferedImage(InputStream)
    }

    constructor(byteArray: ByteArray) : this(ByteArrayInputStream(byteArray))

    constructor(width: Int, height: Int) {
        this.bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        this.g2d = graphics
    }

    constructor(bufferedImage: BufferedImage) {
        this.bufferedImage = bufferedImage
    }


    constructor(img: RenderedImage) {
        if (img is BufferedImage) {
            this.bufferedImage = img
        } else {
            val cm = img.colorModel
            val raster = cm.createCompatibleWritableRaster(img.width, img.height)
            val isAlphaPremultiplied = cm.isAlphaPremultiplied
            val properties = java.util.Hashtable<Any, Any>()
            val keys = img.propertyNames
            if (keys != null) {
                for (i in keys.indices) {
                    properties.put(keys[i], img.getProperty(keys[i]))
                }
            }
            val result = BufferedImage(cm, raster, isAlphaPremultiplied, properties)
            img.copyData(raster)
            this.bufferedImage = result
        }
    }


    //**************************************************************************
    //** Constructor
    //**************************************************************************
    /** Creates a new instance of this class using a block of text.
     * @param fontName Name of the font you with to use. Note that you can get
     * a list of available fonts like this:
     * <pre>
     * for (String fontName : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
     * System.out.println(fontName);
     * }
    </pre> *
     */
    constructor(text: String, fontName: String, fontSize: Int, r: Int, g: Int, b: Int) : this(text, Font(fontName, Font.TRUETYPE_FONT, fontSize), r, g, b)


    constructor(text: String, font: Font, r: Int, g: Int, b: Int) {

        //Get Font Metrics
        val t = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
        t.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)

        val fm = t.getFontMetrics(font)
        val width = fm.stringWidth(text)
        val height = fm.height
        val descent = fm.descent

        t.dispose()


        //Create Image
        bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        g2d = bufferedImage.createGraphics()
        g2d!!.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)

        //Add Text
        val alpha = 1.0f //Set alpha.  0.0f is 100% transparent and 1.0f is 100% opaque.
        g2d!!.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        g2d!!.color = Color(r, g, b)
        g2d!!.font = font
        g2d!!.drawString(text, 0, height - descent)
    }


    //**************************************************************************
    //** setBackgroundColor
    //**************************************************************************
    /** Used to set the background color. Creates an image layer and inserts it
     * under the existing graphic. This method should only be called once.
     */
    fun setBackgroundColor(r: Int, g: Int, b: Int) {
        /*
        Color org = g2d.getColor();
        g2d.setColor(new Color(r,g,b));
        g2d.fillRect(1,1,width-2,height-2); //g2d.fillRect(0,0,width,height);
        g2d.setColor(org);
        */

        var imageType = bufferedImage.type
        if (imageType == 0) {
            imageType = BufferedImage.TYPE_INT_ARGB
        }

        val width = this.width
        val height = this.height

        val bi = BufferedImage(width, height, imageType)
        val g2d = bi.createGraphics()
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)
        g2d.color = Color(r, g, b)
        g2d.fillRect(0, 0, width, height)

        val img = bufferedImage
        g2d.drawImage(img, 0, 0, null)

        this.bufferedImage = bi

        g2d.dispose()
    }


    //**************************************************************************
    //** addText
    //**************************************************************************
    /**  Used to add text to the image at a given position.
     * @param x Lower left coordinate of the text
     * @param y Lower left coordinate of the text
     * @param fontName Name of the font face (e.g. "Tahoma", "Helvetica", etc.)
     * @param fontSize Size of the font
     * @param r Value for the red channel (0-255)
     * @param g Value for the green channel (0-255)
     * @param b Value for the blue channel (0-255)
     */
    fun addText(text: String, x: Int, y: Int, fontName: String, fontSize: Int, r: Int, g: Int, b: Int) {
        addText(text, x, y, Font(fontName, Font.TRUETYPE_FONT, fontSize), r, g, b)
    }


    //**************************************************************************
    //** addText
    //**************************************************************************
    /**  Used to add text to the image at a given position.
     * @param x Lower left coordinate of the text
     * @param y Lower left coordinate of the text
     * @param font Font
     * @param r Value for the red channel (0-255)
     * @param g Value for the green channel (0-255)
     * @param b Value for the blue channel (0-255)
     */
    @JvmOverloads
    fun addText(text: String, x: Int, y: Int, font: Font = Font("SansSerif", Font.TRUETYPE_FONT, 12), r: Int = 0, g: Int = 0, b: Int = 0) {
        g2d = graphics
        g2d!!.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)


        g2d!!.color = Color(r, g, b)
        g2d!!.font = font
        g2d!!.drawString(text, x, y)
    }


    //**************************************************************************
    //** addPoint
    //**************************************************************************
    /**  Simple drawing function used to set color of a specific pixel in the
     * image.
     */
    fun addPoint(x: Int, y: Int, r: Int, g: Int, b: Int) {
        setColor(x, y, Color(r, g, b))
    }


    //**************************************************************************
    //** setColor
    //**************************************************************************
    /**  Used to set the color (ARGB value) for a specific pixel in the image.
     * Note that input x,y values are relative to the upper left corner of the
     * image, starting at 0,0.
     */
    fun setColor(x: Int, y: Int, color: Color) {
        g2d = graphics
        val org = g2d!!.color
        g2d!!.color = color
        g2d!!.fillRect(x, y, 1, 1)
        g2d!!.color = org
    }


    //**************************************************************************
    //** getColor
    //**************************************************************************
    /**  Used to retrieve the color (ARGB) values for a specific pixel in the
     * image. Returns a java.awt.Color object. Note that input x,y values are
     * relative to the upper left corner of the image, starting at 0,0.
     */
    fun getColor(x: Int, y: Int): Color {
        //return new Color(bufferedImage.getRGB(x, y)); //<--This will return an incorrect alpha value

        val pixel = bufferedImage.getRGB(x, y)
        val alpha = pixel shr 24 and 0xff
        val red = pixel shr 16 and 0xff
        val green = pixel shr 8 and 0xff
        val blue = pixel and 0xff
        return java.awt.Color(red, green, blue, alpha)
    }


    //**************************************************************************
    //** addImage
    //**************************************************************************
    /**  Used to add an image "overlay" to the existing image at a given
     * position. This method can also be used to create image mosiacs.
     */
    fun addImage(`in`: BufferedImage?, x: Int, y: Int, expand: Boolean) {
        var x = x
        var y = y

        var x2 = 0
        var y2 = 0
        var w = bufferedImage.width
        var h = bufferedImage.height

        if (expand) {

            //Update Width and Horizontal Position of the Original Image
            if (x < 0) {
                w = w + -x
                if (`in`!!.width > w) {
                    w = w + (`in`.width - w)
                }
                x2 = -x
                x = 0
            } else if (x > w) {
                w = w + (x - w) + `in`!!.width
            } else {
                if (x + `in`!!.width > w) {
                    w = w + (x + `in`.width - w)
                }
            }

            //Update Height and Vertical Position of the Original Image
            if (y < 0) {
                h = h + -y
                if (`in`.height > h) {
                    h = h + (`in`.height - h)
                }
                y2 = -y
                y = 0
            } else if (y > h) {
                h = h + (y - h) + `in`.height
            } else {
                if (y + `in`.height > h) {
                    h = h + (y + `in`.height - h)
                }
            }

        }


        //Create new image "collage"
        if (w > bufferedImage.width || h > bufferedImage.height) {
            var imageType = bufferedImage.type
            if (imageType == 0 || imageType == 12) {
                imageType = BufferedImage.TYPE_INT_ARGB
            }
            val bi = BufferedImage(w, h, imageType)
            val g2d = bi.createGraphics()
            var img = bufferedImage
            g2d.drawImage(img, x2, y2, null)
            img = `in`!!
            g2d.drawImage(img, x, y, null)
            g2d.dispose()
            bufferedImage = bi
        } else {
            val g2d = bufferedImage.createGraphics()
            g2d.drawImage(`in`, x, y, null)
            g2d.dispose()
        }

    }


    //**************************************************************************
    //** addImage
    //**************************************************************************
    /**  Used to add an image "overlay" to the existing image at a given
     * position. This method can also be used to create image mosiacs.
     */
    fun addImage(`in`: LorittaImage, x: Int, y: Int, expand: Boolean) {
        addImage(`in`.bufferedImage, x, y, expand)
    }


    //**************************************************************************
    //** createBufferedImage
    //**************************************************************************
    /** Used to create a BufferedImage from a InputStream  */

    private fun createBufferedImage(input: java.io.InputStream) {
        try {
            //bufferedImage = ImageIO.read(input);

            val stream = ImageIO.createImageInputStream(input)

            val iter = ImageIO.getImageReaders(stream)
            if (!iter.hasNext()) {
                return
            }

            val reader = iter.next() as ImageReader
            val param = reader.defaultReadParam
            reader.setInput(stream, true, true)

            try {
                bufferedImage = reader.read(0, param)
                metadata = reader.getImageMetadata(0)
            } finally {
                reader.dispose()
                stream.close()
            }


            input.close()
        } catch (e: Exception) {
            //e.printStackTrace();
        }

    }


    //**************************************************************************
    //** Rotate
    //**************************************************************************
    /**  Used to rotate the image (clockwise). Rotation angle is specified in
     * degrees relative to the top of the image.
     */
    fun rotate(Degrees: Double) {

        //Define Image Center (Axis of Rotation)
        val width = this.width
        val height = this.height
        var cx = width / 2
        var cy = height / 2

        //create an array containing the corners of the image (TL,TR,BR,BL)
        val corners = intArrayOf(0, 0, width, 0, width, height, 0, height)

        //Define bounds of the image
        var minX: Int
        var minY: Int
        var maxX: Int
        var maxY: Int
        maxX = cx
        minX = maxX
        maxY = cy
        minY = maxY
        val theta = Math.toRadians(Degrees)
        var i = 0
        while (i < corners.size) {

            //Rotates the given point theta radians around (cx,cy)
            val x = Math.round(
                    Math.cos(theta) * (corners[i] - cx) - Math.sin(theta) * (corners[i + 1] - cy) + cx
            ).toInt()

            val y = Math.round(
                    Math.sin(theta) * (corners[i] - cx) +
                            Math.cos(theta) * (corners[i + 1] - cy) + cy.toDouble()
            ).toInt()

            //Update our bounds
            if (x > maxX) maxX = x
            if (x < minX) minX = x
            if (y > maxY) maxY = y
            if (y < minY) minY = y
            i += 2
        }


        //Update Image Center Coordinates
        cx = cx - minX
        cy = cy - minY

        //Create Buffered Image
        var result: BufferedImage? = BufferedImage(maxX - minX, maxY - minY,
                BufferedImage.TYPE_INT_ARGB)

        //Create Graphics
        val g2d = result!!.createGraphics()

        //Enable anti-alias and Cubic Resampling
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON)

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC)

        //Rotate the image
        var xform: AffineTransform? = AffineTransform()
        xform!!.rotate(theta, cx.toDouble(), cy.toDouble())
        g2d.transform = xform
        g2d.drawImage(bufferedImage, -minX, -minY, null)
        g2d.dispose()

        //Update Class Variables
        this.bufferedImage = result

        //Delete Heavy Objects
        result = null
        xform = null
    }


    //**************************************************************************
    //** Rotate Clockwise
    //**************************************************************************
    /**  Rotates the image 90 degrees clockwise  */

    fun rotateClockwise() {
        rotate(90.0)
    }


    //**************************************************************************
    //** Rotate Counter Clockwise
    //**************************************************************************
    /**  Rotates the image -90 degrees  */

    fun rotateCounterClockwise() {
        rotate(-90.0)
    }


    //**************************************************************************
    //** Auto-Rotate
    //**************************************************************************
    /**  Used to automatically rotate the image based on the image metadata
     * (EXIF Orientation tag).
     */
    fun rotate() {
        try {
            val orientation = exifTags!![0x0112] as Int
            when (orientation) {
                1 -> return  //"Top, left side (Horizontal / normal)"
                2 -> flip()
                3 -> rotate(180.0)
                4 -> {
                    flip()
                    rotate(180.0)
                }
                5 -> {
                    flip()
                    rotate(270.0)
                }
                6 -> rotate(90.0)
                7 -> {
                    flip()
                    rotate(90.0)
                }
                8 -> rotate(270.0)
            }//"Top, right side (Mirror horizontal)";
            //"Bottom, right side (Rotate 180)";
            //"Bottom, left side (Mirror vertical)";
            //"Left side, top (Mirror horizontal and rotate 270 CW)";
            //"Right side, top (Rotate 90 CW)";
            //"Right side, bottom (Mirror horizontal and rotate 90 CW)";
            //"Left side, bottom (Rotate 270 CW)";
        } catch (e: Exception) {
            //Failed to parse exif orientation.
        }

    }


    //**************************************************************************
    //** Resize
    //**************************************************************************
    /**  Used to resize an image. Provides the option to maintain the original
     * aspect ratio (relative to the output width).
     */
    @JvmOverloads
    fun resize(Width: Int, Height: Int, maintainRatio: Boolean = false) {

        //long startTime = getStartTime();

        var outputWidth = Width
        var outputHeight = Height

        val width = this.width
        val height = this.height

        if (maintainRatio) {

            var ratio = 0.0

            if (width > height) {
                ratio = Width.toDouble() / width.toDouble()
            } else {
                ratio = Height.toDouble() / height.toDouble()
            }

            val dw = width * ratio
            val dh = height * ratio

            outputWidth = Math.round(dw).toInt()
            outputHeight = Math.round(dh).toInt()

            if (outputWidth > width || outputHeight > height) {
                outputWidth = width
                outputHeight = height
            }
        }


        //Resize the image (create new buffered image)
        var outputImage: java.awt.Image? = bufferedImage.getScaledInstance(outputWidth, outputHeight, BufferedImage.SCALE_AREA_AVERAGING)
        var bi: BufferedImage? = BufferedImage(outputWidth, outputHeight, imageType)
        var g2d: Graphics2D? = bi!!.createGraphics()
        g2d!!.drawImage(outputImage, 0, 0, null)
        g2d.dispose()

        this.bufferedImage = bi

        outputImage = null
        bi = null
        g2d = null
    }


    //**************************************************************************
    //** Set/Update Corners (Skew)
    //**************************************************************************
    /**  Used to skew an image by updating the corner coordinates. Coordinates are
     * supplied in clockwise order starting from the upper left corner.
     */
    fun setCorners(x0: Float, y0: Float, //UL
                   x1: Float, y1: Float, //UR
                   x2: Float, y2: Float, //LR
                   x3: Float, y3: Float) { //LL

        val skew = Skew(this.bufferedImage)
        this.bufferedImage = skew.setCorners(x0, y0, x1, y1, x2, y2, x3, y3)

        if (corners == null)
            corners = arrayListOf<Float>()
        else
            corners!!.clear()
        corners!!.add(x0)
        corners!!.add(y0)
        corners!!.add(x1)
        corners!!.add(y1)
        corners!!.add(x2)
        corners!!.add(y2)
        corners!!.add(x3)
        corners!!.add(y3)
    }


    //**************************************************************************
    //** Get Corners
    //**************************************************************************
    /**  Used to retrieve the corner coordinates of the image. Coordinates are
     * supplied in clockwise order starting from the upper left corner. This
     * information is particularly useful for generating drop shadows, inner
     * and outer glow, and reflections.
     * NOTE: Coordinates are not updated after resize(), rotate(), or addImage()
     */
    fun getCorners(): FloatArray {

        if (corners == null) {
            val w = width.toFloat()
            val h = height.toFloat()
            corners = java.util.ArrayList()
            corners!!.add(0f)
            corners!!.add(0f)
            corners!!.add(w)
            corners!!.add(0f)
            corners!!.add(w)
            corners!!.add(h)
            corners!!.add(0f)
            corners!!.add(h)
        }

        val arr = corners!!.toTypedArray()
        val ret = FloatArray(arr.size)
        for (i in arr.indices) {
            val f = arr[i]
            ret[i] = f
        }
        return ret
    }


    //**************************************************************************
    //** Sharpen
    //**************************************************************************
    /**  Used to sharpen the image using a 3x3 kernal.  */

    fun sharpen() {

        val width = this.width
        val height = this.height

        //define kernal
        val kernel = Kernel(3, 3,
                floatArrayOf(0.0f, -0.2f, 0.0f, -0.2f, 1.8f, -0.2f, 0.0f, -0.2f, 0.0f))

        //apply convolution
        var out = BufferedImage(width, height, imageType)
        val op = ConvolveOp(kernel)
        out = op.filter(bufferedImage, out)

        //replace 2 pixel border created via convolution
        val overlay = out.getSubimage(2, 2, width - 4, height - 4)
        val g2d = bufferedImage.createGraphics()
        g2d.drawImage(overlay, 2, 2, null)
        g2d.dispose()

    }


    //**************************************************************************
    //** Desaturate
    //**************************************************************************
    /**  Used to completely desaturate an image (creates a gray-scale image).  */

    fun desaturate() {
        bufferedImage = desaturate(bufferedImage)
    }


    //**************************************************************************
    //** Desaturate
    //**************************************************************************
    /**  Used to desaturate an image by a specified percentage (expressed as
     * a double or float). The larger the percentage, the greater the
     * desaturation and the "grayer" the image. Valid ranges are from 0-1.
     */

    fun desaturate(percent: Double) {
        val alpha = percent.toFloat()
        val overlay = desaturate(bufferedImage)
        val g2d = bufferedImage.createGraphics()
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        g2d.drawImage(overlay, 0, 0, null)
        g2d.dispose()
    }


    //**************************************************************************
    //** Desaturate (Private Function)
    //**************************************************************************
    /**  Convenience function called by the other 2 desaturation methods.  */

    private fun desaturate(`in`: BufferedImage): BufferedImage {
        val out = BufferedImage(`in`.width, `in`.height, getImageType(`in`))
        val op = ColorConvertOp(
                ColorSpace.getInstance(ColorSpace.CS_GRAY), null)
        return op.filter(`in`, out)
    }


    //**************************************************************************
    //** setOpacity
    //**************************************************************************

    fun setOpacity(percent: Double) {
        var percent = percent
        if (percent > 1) percent = percent / 100
        val alpha = percent.toFloat()
        var imageType = bufferedImage.type
        if (imageType == 0) {
            imageType = BufferedImage.TYPE_INT_ARGB
        }
        val out = BufferedImage(width, height, imageType)
        val g2d = out.createGraphics()
        g2d.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        g2d.drawImage(bufferedImage, 0, 0, null)
        g2d.dispose()
        bufferedImage = out
    }


    //**************************************************************************
    //** Flip (Horizonal)
    //**************************************************************************
    /**  Used to flip an image along it's y-axis (horizontal). Vertical flipping
     * is supported via the rotate method (i.e. rotate +/-180).
     */
    fun flip() {
        val out = BufferedImage(width, height, imageType)
        val tx = AffineTransform.getScaleInstance(-1.0, 1.0)
        tx.translate((-bufferedImage.width).toDouble(), 0.0)
        val op = AffineTransformOp(tx, AffineTransformOp.TYPE_BICUBIC)
        bufferedImage = op.filter(bufferedImage, out)
    }


    //**************************************************************************
    //** Crop
    //**************************************************************************
    /**  Used to subset or crop an image.  */

    fun crop(x: Int, y: Int, width: Int, height: Int) {
        bufferedImage = getSubimage(x, y, width, height)
    }


    //**************************************************************************
    //** copy
    //**************************************************************************
    /** Returns a copy of the current image.  */

    fun copy(): LorittaImage {
        return LorittaImage(bufferedImage)
    }


    //**************************************************************************
    //** copyRect
    //**************************************************************************
    /** Returns a copy of the image at a given rectangle.  */

    fun copyRect(x: Int, y: Int, width: Int, height: Int): LorittaImage {
        return LorittaImage(getSubimage(x, y, width, height))
    }


    //**************************************************************************
    //** getSubimage
    //**************************************************************************
    /** Returns a copy of the image at a given rectangle. In Java 1.6, the
     * BufferedImage.getSubimage() throws an exception if the rectangle falls
     * outside the image bounds. This method was written to overcome this
     * limitation.
     */
    private fun getSubimage(x: Int, y: Int, width: Int, height: Int): BufferedImage {

        val rect1 = Rectangle(0, 0, bufferedImage.width, bufferedImage.height)
        val rect2 = Rectangle(x, y, width, height)


        //If the requested rectangle falls inside the image bounds, simply return
        //the subimage
        if (rect1.contains(rect2)) {
            return bufferedImage.getSubimage(x, y, width, height)
        } else { //requested rectangle falls outside the image bounds!


            //Create buffered image
            var imageType = bufferedImage.type
            if (imageType == 0 || imageType == 12) {
                imageType = BufferedImage.TYPE_INT_ARGB
            }
            val bi = BufferedImage(width, height, imageType)


            //If the requested rectangle intersects the image bounds, crop the
            //the source image and insert it into the BufferedImage
            if (rect1.intersects(rect2)) {

                val g2d = bi.createGraphics()
                var subImage: BufferedImage? = null
                val X: Int
                val Y: Int

                if (x < 0) {
                    val x1 = 0
                    val y1: Int
                    var h: Int
                    var w: Int

                    if (y < 0) {
                        y1 = 0
                        h = y + height
                        Y = height - h
                    } else {
                        y1 = y
                        h = height
                        Y = 0
                    }

                    if (h + y1 > bufferedImage.height) h = bufferedImage.height - y1

                    w = x + width
                    if (w > bufferedImage.width) w = bufferedImage.width


                    subImage = bufferedImage.getSubimage(x1, y1, w, h)

                    X = width - w
                } else {
                    val y1: Int
                    var h: Int
                    var w: Int

                    if (y < 0) {
                        y1 = 0
                        h = y + height
                        Y = height - h
                    } else {
                        y1 = y
                        h = height
                        Y = 0
                    }

                    if (h + y1 > bufferedImage.height) h = bufferedImage.height - y1

                    w = width
                    if (w + x > bufferedImage.width) w = bufferedImage.width - x

                    X = 0

                    subImage = bufferedImage.getSubimage(x, y1, w, h)
                }


                g2d.drawImage(subImage, X, Y, null)
                g2d.dispose()

            }

            return bi
        }
    }


    //**************************************************************************
    //** trim
    //**************************************************************************
    /** Used to remove excess pixels around an image by cropping the image to its
     * "true" extents. Crop bounds are determined by finding pixels that *don't*
     * match the input color. For example, you can trim off excess black pixels
     * around an image by specifying an rgb value of 0,0,0. Similarly, you can
     * trim off pure white pixels around an image by specifying an rgb value of
     * 255,255,255. Note that transparent pixels are considered as null values
     * and will be automatically trimmed from the edges.
     */
    @JvmOverloads
    fun trim(r: Int = 0, g: Int = 0, b: Int = 0) {
        var top = 0
        var bottom = 0
        var left = 0
        var right = 0

        for (y in 0 until bufferedImage.height) {
            for (x in 0 until bufferedImage.width) {
                if (hasColor(bufferedImage.getRGB(x, y), r, g, b)) {
                    bottom = y
                    break
                }
            }
        }

        for (y in bufferedImage.height - 1 downTo -1 + 1) {
            for (x in 0 until bufferedImage.width) {
                if (hasColor(bufferedImage.getRGB(x, y), r, g, b)) {
                    top = y
                    break
                }
            }
        }

        for (x in 0 until bufferedImage.width) {
            for (y in 0 until bufferedImage.height) {
                if (hasColor(bufferedImage.getRGB(x, y), r, g, b)) {
                    right = x
                    break
                }
            }
        }

        for (x in bufferedImage.width - 1 downTo -1 + 1) {
            for (y in 0 until bufferedImage.height) {
                if (hasColor(bufferedImage.getRGB(x, y), r, g, b)) {
                    left = x
                    break
                }
            }
        }

        if (left == right || top == bottom) {
            bufferedImage = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB)
        } else {
            bufferedImage = bufferedImage.getSubimage(left, top, right - left, bottom - top)
        }
    }


    //**************************************************************************
    //** getBufferedImage
    //**************************************************************************
    /**  Used to retrieve a scaled copy of the current image.  */

    fun getBufferedImage(width: Int, height: Int, maintainRatio: Boolean): BufferedImage? {
        val image = LorittaImage(bufferedImage)
        image.resize(width, height, maintainRatio)
        return image.bufferedImage
    }


    //**************************************************************************
    //** getByteArray
    //**************************************************************************
    /** Returns the image as a byte array.  */

    fun getByteArray(format: String): ByteArray? {
        var format = format
        var rgb: ByteArray? = null

        format = format.lowercase()
        if (format.startsWith("image/")) {
            format = format.substring(format.indexOf("/") + 1)
        }

        try {
            if (isJPEG(format)) {
                rgb = getJPEGByteArray(outputQuality)
            } else {
                val bas = ByteArrayOutputStream()
                ImageIO.write(bufferedImage, format.lowercase(), bas)
                rgb = bas.toByteArray()
            }
        } catch (e: Exception) {
        }

        return rgb
    }


    //**************************************************************************
    //** saveAs
    //**************************************************************************
    /**  Exports the image to a file. Output format is determined by the output
     * file extension.
     */
    fun saveAs(PathToImageFile: String) {
        saveAs(java.io.File(PathToImageFile))
    }


    //**************************************************************************
    //** saveAs
    //**************************************************************************
    /**  Exports the image to a file. Output format is determined by the output
     * file extension.
     */
    fun saveAs(OutputFile: java.io.File) {
        try {
            //Create output directory
            OutputFile.parentFile.mkdirs()

            //Write buffered image to disk
            val FileExtension = getExtension(OutputFile.name).lowercase()
            if (isJPEG(FileExtension)) {
                FileOutputStream(OutputFile).use { output -> output.write(getJPEGByteArray(outputQuality)!!) }
            } else {
                var rendImage: RenderedImage? = bufferedImage
                if (isJPEG2000(FileExtension)) {
                    ImageIO.write(rendImage!!, "JPEG 2000", OutputFile)
                } else {
                    ImageIO.write(rendImage!!, FileExtension, OutputFile)
                }
                rendImage = null
            }
            //System.out.println("Output image is " + width + "x" + height + "...");
        } catch (e: Exception) {
            //printError(e);
        }

    }


    /*
    public void setCacheDirectory(java.io.File cacheDirectory){
        try{
            if (cacheDirectory.isFile()){
                cacheDirectory = cacheDirectory.getParentFile();
            }
            cacheDirectory.mkdirs();
            ImageIO.setUseCache(true);
            this.cacheDirectory = cacheDirectory;
        }
        catch(Exception e){
            this.cacheDirectory = null;
        }
    }

    public java.io.File getCacheDirectory(){
        return cacheDirectory;
    }
    */

    //**************************************************************************
    //** setOutputQuality
    //**************************************************************************
    /**  Used to set the output quality/compression ratio. Only applies when
     * creating JPEG images. Applied only when writing the image to a file or
     * byte array.
     */
    fun setOutputQuality(percentage: Double) {
        var percentage = percentage
        if (percentage > 1 && percentage <= 100) percentage = percentage / 100
        var q = percentage.toFloat()
        if (q == 1f && useSunCodec) q = 1.2f
        if (q >= 0f && q <= 1.2f) outputQuality = q
    }


    //**************************************************************************
    //** isJPEG
    //**************************************************************************
    /**  Used to determine whether to create a custom jpeg compressed image    */

    private fun isJPEG(FileExtension: String): Boolean {
        var FileExtension = FileExtension
        FileExtension = FileExtension.trim { it <= ' ' }.lowercase()
        return FileExtension == "jpg" ||
                FileExtension == "jpeg" ||
                FileExtension == "jpe" ||
                FileExtension == "jff"
    }


    //**************************************************************************
    //** isJPEG2000
    //**************************************************************************
    /**  Used to determine whether to create a custom jpeg compressed image    */

    private fun isJPEG2000(FileExtension: String): Boolean {
        var FileExtension = FileExtension
        FileExtension = FileExtension.trim { it <= ' ' }.lowercase()
        return FileExtension == "jp2" ||
                FileExtension == "jpc" ||
                FileExtension == "j2k" ||
                FileExtension == "jpx"
    }


    //**************************************************************************
    //** getJPEGByteArray
    //**************************************************************************
    /** Returns a JPEG compressed byte array.  */

    @Throws(IOException::class)
    private fun getJPEGByteArray(outputQuality: Float): ByteArray? {
        var outputQuality = outputQuality
        if (outputQuality >= 0f && outputQuality <= 1.2f) {
            val bas = ByteArrayOutputStream()
            var bi = bufferedImage
            val t = bufferedImage.transparency

            //if (t==BufferedImage.BITMASK) System.out.println("BITMASK");
            //if (t==BufferedImage.OPAQUE) System.out.println("OPAQUE");

            if (t == BufferedImage.TRANSLUCENT) {
                bi = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                val biContext = bi.createGraphics()
                biContext.drawImage(bufferedImage, 0, 0, null)
            }


            //First we will try to compress the image using the com.sun.image.codec.jpeg
            //package. These classes are marked as deprecated in JDK 1.7 and several
            //users have reported problems with this method. Instead, we are
            //supposed to use the JPEGImageWriteParam class. However, I have not
            //been able to adequatly test the compression quality or find an
            //anology to the setHorizontalSubsampling and setVerticalSubsampling
            //methods. Therefore, we will attempt to compress the image using the
            //com.sun.image.codec.jpeg package. If the compression fails, we will
            //use the JPEGImageWriteParam.
            if (useSunCodec) {


                try {

                    //For Java 1.7 users, we will try to invoke the Sun JPEG Codec using reflection
                    val encoder = JPEGCodec!!.getMethod("createJPEGEncoder", java.io.OutputStream::class.java).invoke(
                            JPEGCodec, bas)
                    val params = JPEGCodec!!.getMethod("getDefaultJPEGEncodeParam", BufferedImage::class.java).invoke(
                            JPEGCodec, bi)
                    params.javaClass.getMethod("setQuality", Float::class.javaPrimitiveType, Boolean::class.javaPrimitiveType).invoke(params, outputQuality, true)
                    params.javaClass.getMethod("setHorizontalSubsampling", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType).invoke(params, 0, 2)
                    params.javaClass.getMethod("setVerticalSubsampling", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType).invoke(params, 0, 2)
                    encoder.javaClass.getMethod("encode", BufferedImage::class.java, JPEGEncodeParam).invoke(encoder, bi, params)


                    //Here's the original compression code without reflection
                    /*
                    JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bas);
                    JPEGEncodeParam params = JPEGCodec.getDefaultJPEGEncodeParam(bi);
                    params.setQuality(outputQuality, true); //true
                    params.setHorizontalSubsampling(0,2);
                    params.setVerticalSubsampling(0,2);
                    encoder.encode(bi, params);
                    */
                } catch (e: Exception) {
                    bas.reset()
                }

            }


            //If the com.sun.image.codec.jpeg package is not found or if the
            //compression failed, we will use the JPEGImageWriteParam class.
            if (bas.size() == 0) {

                if (outputQuality > 1f) outputQuality = 1f

                val writer = ImageIO.getImageWritersByFormatName("jpg").next()
                val params = writer.defaultWriteParam as JPEGImageWriteParam
                params.compressionMode = ImageWriteParam.MODE_EXPLICIT
                params.compressionQuality = outputQuality
                writer.output = ImageIO.createImageOutputStream(bas)
                writer.write(null, IIOImage(bi, null, null), params)
            }


            bas.flush()
            return bas.toByteArray()
        } else {
            return byteArray
        }
    }

    private fun getImageType(bufferedImage: BufferedImage): Int {
        var i = bufferedImage.type
        if (i <= 0) i = BufferedImage.TYPE_INT_ARGB //<- is this ok?
        return i
    }


    //**************************************************************************
    //** getExtension
    //**************************************************************************
    /**  Returns the file extension for a given file name, if one exists.  */

    private fun getExtension(FileName: String): String {
        return if (FileName.contains("." as CharSequence)) {
            FileName.substring(FileName.lastIndexOf(".") + 1, FileName.length)
        } else {
            ""
        }
    }


    //**************************************************************************
    //** hasColor
    //**************************************************************************
    /** Used to determine whether a given pixel has a color value. Returns false
     * if the pixel matches the input color or is transparent.
     */
    private fun hasColor(pixel: Int, red: Int, green: Int, blue: Int): Boolean {

        val a = pixel shr 24 and 0xff
        val r = pixel shr 16 and 0xff
        val g = pixel shr 8 and 0xff
        val b = pixel and 0xff

        return !(r == red && g == green && b == blue || a == 0)
    }


    //**************************************************************************
    //** equals
    //**************************************************************************
    /**  Used to compare this image to another. If the ARGB values match, this
     * method will return true.
     */
    override fun equals(obj: Any?): Boolean {
        if (obj != null) {
            if (obj is LorittaImage) {
                val image = obj as LorittaImage?
                if (image!!.width == this.width && image.height == this.height) {

                    //Iterate through all the pixels in the image and compare RGB values
                    for (i in 0 until image.width) {
                        for (j in 0 until image.height) {

                            if (image.getColor(i, j) != this.getColor(i, j)) {
                                return false
                            }
                        }

                    }

                    return true
                }

            }
        }
        return false
    }


    /** Private method used to initialize the exif and gps hashmaps  */
    private fun parseExif() {

        exif = HashMap()
        gps = HashMap()
        for (marker in getUnknownTags(0xE1)) {
            val exifData = marker.userObject as ByteArray

            var metadataParser: MetadataParser? = MetadataParser(exifData, 0xE1)
            val exif = metadataParser!!.getTags("EXIF")
            val gps = metadataParser.getTags("GPS")

            if (exif != null) this.exif!!.putAll(exif)
            if (gps != null) this.gps!!.putAll(gps)

            metadataParser = null
        }
    }


    private fun getCoordinate(RationalArray: String): Double {

        //num + "/" + den
        val arr = RationalArray.substring(1, RationalArray.length - 1).split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val deg = arr[0].trim { it <= ' ' }.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val min = arr[1].trim { it <= ' ' }.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val sec = arr[2].trim { it <= ' ' }.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val degNumerator = java.lang.Double.parseDouble(deg[0])
        var degDenominator = 1.0
        try {
            degDenominator = java.lang.Double.parseDouble(deg[1])
        } catch (e: Exception) {
        }

        val minNumerator = java.lang.Double.parseDouble(min[0])
        var minDenominator = 1.0
        try {
            minDenominator = java.lang.Double.parseDouble(min[1])
        } catch (e: Exception) {
        }

        val secNumerator = java.lang.Double.parseDouble(sec[0])
        var secDenominator = 1.0
        try {
            secDenominator = java.lang.Double.parseDouble(sec[1])
        } catch (e: Exception) {
        }

        var m = 0.0
        if (degDenominator != 0.0 || degNumerator != 0.0) {
            m = degNumerator / degDenominator
        }

        if (minDenominator != 0.0 || minNumerator != 0.0) {
            m += minNumerator / minDenominator / 60.0
        }

        if (secDenominator != 0.0 || secNumerator != 0.0) {
            m += secNumerator / secDenominator / 3600.0
        }

        return m
    }


    //**************************************************************************
    //** getUnknownTags
    //**************************************************************************
    /** Returns a list of "unknown" IIOMetadataNodes for a given MarkerTag. You
     * can use this method to retrieve EXIF, IPTC, XPM, and other format
     * specific metadata. Example:
     * <pre>
     * byte[] IptcData = (byte[]) metadata.getUnknownTags(0xED)[0].getUserObject();
     * byte[] ExifData = (byte[]) metadata.getUnknownTags(0xE1)[0].getUserObject();
    </pre> *
     */
    fun getUnknownTags(MarkerTag: Int): Array<IIOMetadataNode> {
        val markers = java.util.ArrayList<IIOMetadataNode>()
        if (metadata != null)
            for (name in metadata!!.metadataFormatNames) {
                val node = metadata!!.getAsTree(name) as IIOMetadataNode
                val unknownNodes = getElementsByTagName("unknown", node)
                for (unknownNode in unknownNodes) {
                    try {
                        val marker = Integer.parseInt(getAttributeValue(unknownNode.attributes, "MarkerTag"))
                        if (marker == MarkerTag) markers.add(unknownNode as IIOMetadataNode)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        return markers.toTypedArray()
    }

    //**************************************************************************
    //** getMetadataByTagName
    //**************************************************************************
    /** Returns a list of IIOMetadataNodes for a given tag name (e.g. "Chroma",
     * "Compression", "Data", "Dimension", "Transparency", etc).
     * <pre>
     * //Print unknown tags
     * for (IIOMetadataNode unknownNode : metadata.getMetadataByTagName("unknown")){
     * int marker = Integer.parseInt(javaxt.xml.DOM.getAttributeValue(unknownNode, "MarkerTag"));
     * System.out.println(marker + "\t" + "0x" + Integer.toHexString(marker));
     * }
    </pre> *
     */
    fun getMetadataByTagName(tagName: String): Array<IIOMetadataNode> {
        val tags = java.util.ArrayList<IIOMetadataNode>()
        if (metadata != null)
            for (name in metadata!!.metadataFormatNames) {
                val node = metadata!!.getAsTree(name) as IIOMetadataNode
                val unknownNodes = getElementsByTagName(tagName, node)
                for (unknownNode in unknownNodes) {
                    tags.add(unknownNode as IIOMetadataNode)
                }
            }
        return tags.toTypedArray()
    }


    //******************************************************************************
    //**  MetadataParser Class
    //******************************************************************************
    /**
     * Used to decode EXIF and IPTC metadata. Adapted from 2 classes developed by
     * Norman Walsh and released under the W3C open source license. The original
     * exif classes can be found in the W3C Jigsaw project in the
     * org.w3c.tools.jpeg package.
     *
     * @author  Norman Walsh
     * @copyright Copyright (c) 2003 Norman Walsh
     */

    private class MetadataParser(data: ByteArray?, marker: Int) {

        // Implementation notes:
        // (1) Merged Version 1.1 of the "Exif.java" and "ExifData.java" classes.
        // (2) Added new IPTC metadata parser.
        // (3) All unsigned integers are treated as signed ints (should be longs).
        // (4) Added logic to parse GPS Info using the GPS IFD offset value (tag 34853,
        //     hex 0x8825).
        // (5) Added logic to parse an array of rational numbers (e.g. GPS metadata).
        // (6) Improved performance in the parseExif() method by serializing only the
        //     first 8 characters into a string (vs the entire EXIF byte array).
        // (7) TODO: Need to come up with a clever scheme to parse MakerNotes.

        private val bytesPerFormat = intArrayOf(0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8)
        private val NUM_FORMATS = 12
        private val FMT_BYTE = 1
        private val FMT_STRING = 2
        private val FMT_USHORT = 3
        private val FMT_ULONG = 4
        private val FMT_URATIONAL = 5
        private val FMT_SBYTE = 6
        //private final int FMT_UNDEFINED = 7;
        private val FMT_SSHORT = 8
        private val FMT_SLONG = 9
        private val FMT_SRATIONAL = 10
        //private final int FMT_SINGLE = 11;
        //private final int FMT_DOUBLE = 12;

        private var data: ByteArray? = null
        private var intelOrder = false

        private val TAG_EXIF_OFFSET = 0x8769
        private val TAG_INTEROP_OFFSET = 0xa005
        private val TAG_GPS_OFFSET = 0x8825
        private val TAG_USERCOMMENT = 0x9286

        private val tags = HashMap<String, HashMap<Int, Any>>()


        init {
            var data = data
            when (marker) {
                0xED -> parseIptc(data)
                0xE1 -> parseExif(data)
            }

            data = null
        }


        //**************************************************************************
        //** parseIptc
        //**************************************************************************
        /** Used to parse IPTC metadata
         */
        private fun parseIptc(iptcData: ByteArray?) {

            val tags = HashMap<Int, Any>()
            this.tags["IPTC"] = tags

            data = iptcData

            var offset = 0
            while (offset < data!!.size) {
                if (data!![offset].toInt() == 0x1c) {

                    offset++

                    val directoryType: Int
                    val tagType: Int
                    val tagByteCount: Int
                    try {
                        directoryType = data!![offset++].toInt()
                        tagType = data!![offset++].toInt()
                        tagByteCount = get16u(offset)
                        offset += 2
                    } catch (e: Exception) {
                        return
                    }


                    val tagIdentifier = tagType or (directoryType shl 8)

                    var str = ""
                    if (tagByteCount < 1 || tagByteCount > data!!.size - offset) {
                    } else {
                        try {
                            str = String(data!!, offset, tagByteCount, Charsets.UTF_8)
                            offset += tagByteCount
                        } catch (e: Exception) {
                        }

                    }
                    tags[tagIdentifier] = str
                } else {
                    offset++
                }
            }
        }


        //**************************************************************************
        //** parseExif
        //**************************************************************************
        /** Used to parse EXIF metadata
         */
        fun parseExif(exifData: ByteArray?) {

            val tags = HashMap<Int, Any>()
            this.tags["EXIF"] = tags


            try {
                val dataStr = String(exifData!!, 0, 8, Charsets.UTF_8) //new String(exifData);
                if (exifData.size <= 4 || "Exif" != dataStr.substring(0, 4)) {
                    //System.err.println("Not really EXIF data");
                    return
                }

                val byteOrderMarker = dataStr.substring(6, 8)
                if ("II" == byteOrderMarker) {
                    intelOrder = true
                } else if ("MM" == byteOrderMarker) {
                    intelOrder = false
                } else {
                    //System.err.println("Incorrect byte order in EXIF data.");
                    return
                }
            } catch (e: Exception) {
                return
            }


            data = exifData

            val checkValue = get16u(8)
            if (checkValue != 0x2a) {
                data = null
                //System.err.println("Check value fails: 0x"+ Integer.toHexString(checkValue));
                return
            }


            if (data == null) return


            val firstOffset = get32u(10)
            processExifDir(6 + firstOffset, 6, tags)
        }


        //**************************************************************************
        //** getTags
        //**************************************************************************
        /** Returns key/value pairs representing the EXIF or IPTC data.
         */
        fun getTags(dir: String): HashMap<Int, Any>? {
            return tags[dir]
        }


        private fun processExifDir(dirStart: Int, offsetBase: Int, tags: HashMap<Int, Any>?) {
            var tags = tags
            if (dirStart >= data!!.size) return


            val numEntries = get16u(dirStart)
            for (de in 0 until numEntries) {
                val dirOffset = dirStart + 2 + 12 * de

                val tag = get16u(dirOffset)
                val format = get16u(dirOffset + 2)
                val components = get32u(dirOffset + 4)

                //System.err.println("EXIF: entry: 0x" + Integer.toHexString(tag)
                //		 + " " + format
                //		 + " " + components);

                if (format < 0 || format > NUM_FORMATS) {
                    //System.err.println("Bad number of formats in EXIF dir: " + format);
                    return
                }

                val byteCount = components * bytesPerFormat[format]
                var valueOffset = dirOffset + 8

                if (byteCount > 4) {
                    val offsetVal = get32u(dirOffset + 8)
                    valueOffset = offsetBase + offsetVal
                }

                if (tag == TAG_EXIF_OFFSET || tag == TAG_INTEROP_OFFSET || tag == TAG_GPS_OFFSET) {

                    var dirName = ""
                    when (tag) {
                        TAG_EXIF_OFFSET -> dirName = "EXIF"
                        TAG_INTEROP_OFFSET -> dirName = "EXIF"
                        TAG_GPS_OFFSET -> dirName = "GPS"
                    }

                    tags = this.tags[dirName]
                    if (tags == null) {
                        tags = HashMap()
                        this.tags[dirName] = tags
                    }

                    val subdirOffset = get32u(valueOffset)
                    processExifDir(offsetBase + subdirOffset, offsetBase, tags)

                } else {

                    when (format) {
                        FMT_STRING -> {
                            val value = getString(valueOffset, byteCount)
                            if (value != null) tags!![tag] = value
                        }
                        FMT_SBYTE, FMT_BYTE, FMT_USHORT, FMT_SSHORT, FMT_ULONG, FMT_SLONG -> tags!![tag] = getDouble(format, valueOffset).toInt()
                        FMT_URATIONAL, FMT_SRATIONAL ->

                            if (components > 1) {

                                //Create a string representing an array of rational numbers
                                val str = StringBuffer()
                                str.append("[")
                                for (i in 0 until components) {
                                    str.append(getRational(valueOffset + 8 * i))
                                    if (i < components - 1) str.append(",")
                                }
                                str.append("]")
                                tags!![tag] = str.toString()
                            } else {
                                tags!![tag] = getRational(valueOffset)
                            }


                        else //including FMT_UNDEFINED
                        -> {
                            val result = getUndefined(valueOffset, byteCount)
                            if (result != null) tags!![tag] = result
                        }
                    }

                }//else if (tag==0x927c){ //Maker Note
                //TODO: Come up with a clever way to process the Maker Note
                //data = java.util.Arrays.copyOfRange(data, valueOffset, byteCount);
                //tags = new HashMap<Integer, String>();
                //processExifDir(0, 6);
                //}
            }
        }

        //**************************************************************************
        //** getRational
        //**************************************************************************
        /** Returns a string representation of a rational number (numerator and
         * denominator separated with a "/" character).
         */
        private fun getRational(offset: Int): String {
            var num = get32s(offset)
            var den = get32s(offset + 4)
            var result = ""

            // This is a bit silly, I really ought to find a real GCD algorithm
            if (num % 10 == 0 && den % 10 == 0) {
                num = num / 10
                den = den / 10
            }

            if (num % 5 == 0 && den % 5 == 0) {
                num = num / 5
                den = den / 5
            }

            if (num % 3 == 0 && den % 3 == 0) {
                num = num / 3
                den = den / 3
            }

            if (num % 2 == 0 && den % 2 == 0) {
                num = num / 2
                den = den / 2
            }

            if (den == 0) {
                result = "0"
            } else if (den == 1) {
                result = "" + num // "" + int sure looks ugly...
            } else {
                result = "$num/$den"
            }
            return result
        }

        private fun get16s(offset: Int): Int {
            var hi: Int
            var lo: Int

            if (intelOrder) {
                hi = data!![offset + 1].toInt()
                lo = data!![offset].toInt()
            } else {
                hi = data!![offset].toInt()
                lo = data!![offset + 1].toInt()
            }

            lo = lo and 0xFF
            hi = hi and 0xFF

            return (hi shl 8) + lo
        }

        private fun get16u(offset: Int): Int {
            val value = get16s(offset)
            return value and 0xFFFF
        }

        private fun get32s(offset: Int): Int {
            val n1: Int
            val n2: Int
            val n3: Int
            val n4: Int

            if (intelOrder) {
                n1 = data!![offset + 3].toInt() and 0xFF
                n2 = data!![offset + 2].toInt() and 0xFF
                n3 = data!![offset + 1].toInt() and 0xFF
                n4 = data!![offset].toInt() and 0xFF
            } else {
                n1 = data!![offset].toInt() and 0xFF
                n2 = data!![offset + 1].toInt() and 0xFF
                n3 = data!![offset + 2].toInt() and 0xFF
                n4 = data!![offset + 3].toInt() and 0xFF
            }

            return (n1 shl 24) + (n2 shl 16) + (n3 shl 8) + n4
        }

        private fun get32u(offset: Int): Int {
            return get32s(offset) //Should probably return a long instead...
        }

        private fun getUndefined(offset: Int, length: Int): ByteArray {
            return java.util.Arrays.copyOfRange(data!!, offset, offset + length)
        }

        private fun getString(offset: Int, length: Int): String? {
            try {
                return String(data!!, offset, length, Charsets.UTF_8).trim { it <= ' ' }
            } catch (e: Exception) {
                return null
            }

        }

        //**************************************************************************
        //** getDouble
        //**************************************************************************
        /** Used convert a byte into a double. Note that this method used to be
         * called convertAnyValue().
         */
        private fun getDouble(format: Int, offset: Int): Double {
            when (format) {
                FMT_SBYTE -> return data!![offset].toDouble()
                FMT_BYTE -> {
                    val iValue = data!![offset].toInt()
                    return (iValue and 0xFF).toDouble()
                }
                FMT_USHORT -> return get16u(offset).toDouble()
                FMT_ULONG -> return get32u(offset).toDouble()
                FMT_URATIONAL, FMT_SRATIONAL -> {
                    val num = get32s(offset)
                    val den = get32s(offset + 4)
                    return if (den == 0)
                        0.0
                    else
                        num.toDouble() / den.toDouble()
                }
                FMT_SSHORT -> return get16s(offset).toDouble()
                FMT_SLONG -> return get32s(offset).toDouble()
                else -> return 0.0
            }
        }
    }


    //***************************************************************************
    //**  Skew Class
    //***************************************************************************
    /**
     * Used to skew an image. Adapted from 2 image processing classes developed
     * by Jerry Huxtable (http://www.jhlabs.com) and released under
     * the Apache License, Version 2.0.
     *
     */

    private class Skew(private val src: BufferedImage) {

        protected var edgeAction = ZERO
        protected var interpolation = BILINEAR

        protected lateinit var transformedSpace: Rectangle
        protected lateinit var originalSpace: Rectangle

        private var x0: Float = 0.toFloat()
        private var y0: Float = 0.toFloat()
        private var x1: Float = 0.toFloat()
        private var y1: Float = 0.toFloat()
        private var x2: Float = 0.toFloat()
        private var y2: Float = 0.toFloat()
        private var x3: Float = 0.toFloat()
        private var y3: Float = 0.toFloat()
        private var dx1: Float = 0.toFloat()
        private var dy1: Float = 0.toFloat()
        private var dx2: Float = 0.toFloat()
        private var dy2: Float = 0.toFloat()
        private var dx3: Float = 0.toFloat()
        private var dy3: Float = 0.toFloat()
        private var A: Float = 0.toFloat()
        private var B: Float = 0.toFloat()
        private var C: Float = 0.toFloat()
        private var D: Float = 0.toFloat()
        private var E: Float = 0.toFloat()
        private var F: Float = 0.toFloat()
        private var G: Float = 0.toFloat()
        private var H: Float = 0.toFloat()
        private var I: Float = 0.toFloat()
        private val dst: BufferedImage


        val originX: Float
            get() = x0 - Math.min(Math.min(x0, x1), Math.min(x2, x3)).toInt()

        val originY: Float
            get() = y0 - Math.min(Math.min(y0, y1), Math.min(y2, y3)).toInt()


        init {
            this.dst = BufferedImage(src.width, src.height, src.type)
        }

        constructor(src: LorittaImage) : this(src.bufferedImage)


        fun setCorners(x0: Float, y0: Float,
                       x1: Float, y1: Float,
                       x2: Float, y2: Float,
                       x3: Float, y3: Float): BufferedImage {
            this.x0 = x0
            this.y0 = y0
            this.x1 = x1
            this.y1 = y1
            this.x2 = x2
            this.y2 = y2
            this.x3 = x3
            this.y3 = y3

            dx1 = x1 - x2
            dy1 = y1 - y2
            dx2 = x3 - x2
            dy2 = y3 - y2
            dx3 = x0 - x1 + x2 - x3
            dy3 = y0 - y1 + y2 - y3

            val a11: Float
            val a12: Float
            val a13: Float
            val a21: Float
            val a22: Float
            val a23: Float
            val a31: Float
            val a32: Float

            if (dx3 == 0f && dy3 == 0f) {
                a11 = x1 - x0
                a21 = x2 - x1
                a31 = x0
                a12 = y1 - y0
                a22 = y2 - y1
                a32 = y0
                a23 = 0f
                a13 = a23
            } else {
                a13 = (dx3 * dy2 - dx2 * dy3) / (dx1 * dy2 - dy1 * dx2)
                a23 = (dx1 * dy3 - dy1 * dx3) / (dx1 * dy2 - dy1 * dx2)
                a11 = x1 - x0 + a13 * x1
                a21 = x3 - x0 + a23 * x3
                a31 = x0
                a12 = y1 - y0 + a13 * y1
                a22 = y3 - y0 + a23 * y3
                a32 = y0
            }

            A = a22 - a32 * a23
            B = a31 * a23 - a21
            C = a21 * a32 - a31 * a22
            D = a32 * a13 - a12
            E = a11 - a31 * a13
            F = a31 * a12 - a11 * a32
            G = a12 * a23 - a22 * a13
            H = a21 * a13 - a11 * a23
            I = a11 * a22 - a21 * a12

            return filter(src, dst)
        }

        protected fun transformSpace(rect: Rectangle) {
            rect.x = Math.min(Math.min(x0, x1), Math.min(x2, x3)).toInt()
            rect.y = Math.min(Math.min(y0, y1), Math.min(y2, y3)).toInt()
            rect.width = Math.max(Math.max(x0, x1), Math.max(x2, x3)).toInt() - rect.x
            rect.height = Math.max(Math.max(y0, y1), Math.max(y2, y3)).toInt() - rect.y
        }

        private fun filter(src: BufferedImage, dst: BufferedImage?): BufferedImage {
            var dst = dst
            val width = src.width
            val height = src.height
            //int type = src.getType();
            //WritableRaster srcRaster = src.getRaster();

            originalSpace = Rectangle(0, 0, width, height)
            transformedSpace = Rectangle(0, 0, width, height)
            transformSpace(transformedSpace)

            if (dst == null) {
                val dstCM = src.colorModel
                dst = BufferedImage(
                        dstCM,
                        dstCM.createCompatibleWritableRaster(transformedSpace.width, transformedSpace.height),
                        dstCM.isAlphaPremultiplied, null
                )
            }
            //WritableRaster dstRaster = dst.getRaster();

            val inPixels = getRGB(src, 0, 0, width, height, null)

            if (interpolation == NEAREST_NEIGHBOUR)
                return filterPixelsNN(dst, width, height, inPixels, transformedSpace)

            val srcWidth1 = width - 1
            val srcHeight1 = height - 1
            val outWidth = transformedSpace.width
            val outHeight = transformedSpace.height
            val outX: Int
            val outY: Int
            //int index = 0;
            val outPixels = IntArray(outWidth)

            outX = transformedSpace.x
            outY = transformedSpace.y
            val out = FloatArray(2)

            for (y in 0 until outHeight) {
                for (x in 0 until outWidth) {
                    transformInverse(outX + x, outY + y, out)
                    val srcX = Math.floor(out[0].toDouble()).toInt()
                    val srcY = Math.floor(out[1].toDouble()).toInt()
                    val xWeight = out[0] - srcX
                    val yWeight = out[1] - srcY
                    val nw: Int
                    val ne: Int
                    val sw: Int
                    val se: Int

                    if (srcX >= 0 && srcX < srcWidth1 && srcY >= 0 && srcY < srcHeight1) {
                        // Easy case, all corners are in the image
                        val i = width * srcY + srcX
                        nw = inPixels[i]
                        ne = inPixels[i + 1]
                        sw = inPixels[i + width]
                        se = inPixels[i + width + 1]
                    } else {
                        // Some of the corners are off the image
                        nw = getPixel(inPixels, srcX, srcY, width, height)
                        ne = getPixel(inPixels, srcX + 1, srcY, width, height)
                        sw = getPixel(inPixels, srcX, srcY + 1, width, height)
                        se = getPixel(inPixels, srcX + 1, srcY + 1, width, height)
                    }
                    outPixels[x] = bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se)
                }

                if (0 > outY + y) {
                    // Outside in the Y axis, just ignore it since we always write only 1 pixel height
                    continue
                }

                if (0 > outX) {
                    // Outside in the X axis, try to manipulate stuff a bit!
                    val absoluteOutX = Math.abs(outX)

                    val outPixelsDropped = outPixels.drop(absoluteOutX)
                            .toIntArray()

                    // println("Trying to draw at ${outY + y} with a ${outPixelsDropped.size} size")
                    // println("BufferedImage Width: " + dst.width)
                    // println("BufferedImage Height: " + dst.height)
                    setRGB(dst, 0, outY + y, outPixelsDropped.size, 1, outPixelsDropped)
                    continue
                }

                if (outX + transformedSpace.width > src.width) {
                    val diff = src.width - outX
                    // println("Taking ${src.width - outX} pixels")

                    if (0 >= diff)
                        continue

                    // Outside in the X axis in the right direction
                    val outPixelsDropped = outPixels.take(diff)
                            .toIntArray()

                    setRGB(dst, outX, outY + y, outPixelsDropped.size, 1, outPixelsDropped)
                    continue
                }

                setRGB(dst, outX, outY + y, transformedSpace.width, 1, outPixels)
            }
            return dst
        }

        private fun getPixel(pixels: IntArray, x: Int, y: Int, width: Int, height: Int): Int {
            if (x < 0 || x >= width || y < 0 || y >= height) {
                when (edgeAction) {
                    ZERO -> return 0
                    WRAP -> return pixels[mod(y, height) * width + mod(x, width)]
                    CLAMP -> return pixels[clamp(y, 0, height - 1) * width + clamp(x, 0, width - 1)]
                    else -> return 0
                }
            }
            return pixels[y * width + x]
        }


        protected fun filterPixelsNN(dst: BufferedImage, width: Int,
                                     height: Int, inPixels: IntArray, transformedSpace: Rectangle): BufferedImage {
            val outWidth = transformedSpace.width
            val outHeight = transformedSpace.height
            val outX: Int
            val outY: Int
            var srcX: Int
            var srcY: Int
            val outPixels = IntArray(outWidth)

            outX = transformedSpace.x
            outY = transformedSpace.y
            val rgb = IntArray(4)
            val out = FloatArray(2)

            for (y in 0 until outHeight) {
                for (x in 0 until outWidth) {
                    transformInverse(outX + x, outY + y, out)
                    srcX = out[0].toInt()
                    srcY = out[1].toInt()
                    // int casting rounds towards zero, so we check out[0] < 0, not srcX < 0
                    if (out[0] < 0 || srcX >= width || out[1] < 0 || srcY >= height) {
                        val p: Int
                        when (edgeAction) {
                            ZERO -> p = 0
                            WRAP -> p = inPixels[mod(srcY, height) * width + mod(srcX, width)]
                            CLAMP -> p = inPixels[clamp(srcY, 0, height - 1) * width + clamp(srcX, 0, width - 1)]
                            else -> p = 0
                        }
                        outPixels[x] = p
                    } else {
                        val i = width * srcY + srcX
                        rgb[0] = inPixels[i]
                        outPixels[x] = inPixels[i]
                    }
                }
                setRGB(dst, 0, y, transformedSpace.width, 1, outPixels)
            }
            return dst
        }


        protected fun transformInverse(x: Int, y: Int, out: FloatArray) {
            out[0] = originalSpace.width * (A * x + B * y + C) / (G * x + H * y + I)
            out[1] = originalSpace.height * (D * x + E * y + F) / (G * x + H * y + I)
        }

        /*
    public Rectangle2D getBounds2D( BufferedImage src ) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    public Point2D getPoint2D( Point2D srcPt, Point2D dstPt ) {
        if ( dstPt == null )
            dstPt = new Point2D.Double();
        dstPt.setLocation( srcPt.getX(), srcPt.getY() );
        return dstPt;
    }
*/

        /**
         * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance
         * penalty of BufferedImage.getRGB unmanaging the image.
         */
        fun getRGB(image: BufferedImage, x: Int, y: Int, width: Int, height: Int, pixels: IntArray?): IntArray {
            val type = image.type
            return if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) image.raster.getDataElements(x, y, width, height, pixels) as IntArray else image.getRGB(x, y, width, height, pixels, 0, width)
        }

        /**
         * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance
         * penalty of BufferedImage.setRGB unmanaging the image.
         */
        fun setRGB(image: BufferedImage, x: Int, y: Int, width: Int, height: Int, pixels: IntArray) {
            val type = image.type
            if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB)
                image.raster.setDataElements(x, y, width, height, pixels)
            else
                image.setRGB(x, y, width, height, pixels, 0, width)
        }


        /**
         * Clamp a value to an interval.
         * @param a the lower clamp threshold
         * @param b the upper clamp threshold
         * @param x the input parameter
         * @return the clamped value
         */
        private fun clamp(x: Float, a: Float, b: Float): Float {
            return if (x < a) a else if (x > b) b else x
        }

        /**
         * Clamp a value to an interval.
         * @param a the lower clamp threshold
         * @param b the upper clamp threshold
         * @param x the input parameter
         * @return the clamped value
         */
        private fun clamp(x: Int, a: Int, b: Int): Int {
            return if (x < a) a else if (x > b) b else x
        }

        /**
         * Return a mod b. This differs from the % operator with respect to negative numbers.
         * @param a the dividend
         * @param b the divisor
         * @return a mod b
         */
        private fun mod(a: Double, b: Double): Double {
            var a = a
            val n = (a / b).toInt()

            a -= n * b
            return if (a < 0) a + b else a
        }

        /**
         * Return a mod b. This differs from the % operator with respect to negative numbers.
         * @param a the dividend
         * @param b the divisor
         * @return a mod b
         */
        private fun mod(a: Float, b: Float): Float {
            var a = a
            val n = (a / b).toInt()

            a -= n * b
            return if (a < 0) a + b else a
        }

        /**
         * Return a mod b. This differs from the % operator with respect to negative numbers.
         * @param a the dividend
         * @param b the divisor
         * @return a mod b
         */
        private fun mod(a: Int, b: Int): Int {
            var a = a
            val n = a / b

            a -= n * b
            return if (a < 0) a + b else a
        }


        /**
         * Bilinear interpolation of ARGB values.
         * @param x the X interpolation parameter 0..1
         * @param y the y interpolation parameter 0..1
         * @return the interpolated value
         */
        private fun bilinearInterpolate(x: Float, y: Float, nw: Int, ne: Int, sw: Int, se: Int): Int {
            var m0: Float
            var m1: Float
            val a0 = nw shr 24 and 0xff
            val r0 = nw shr 16 and 0xff
            val g0 = nw shr 8 and 0xff
            val b0 = nw and 0xff
            val a1 = ne shr 24 and 0xff
            val r1 = ne shr 16 and 0xff
            val g1 = ne shr 8 and 0xff
            val b1 = ne and 0xff
            val a2 = sw shr 24 and 0xff
            val r2 = sw shr 16 and 0xff
            val g2 = sw shr 8 and 0xff
            val b2 = sw and 0xff
            val a3 = se shr 24 and 0xff
            val r3 = se shr 16 and 0xff
            val g3 = se shr 8 and 0xff
            val b3 = se and 0xff

            val cx = 1.0f - x
            val cy = 1.0f - y

            m0 = cx * a0 + x * a1
            m1 = cx * a2 + x * a3
            val a = (cy * m0 + y * m1).toInt()

            m0 = cx * r0 + x * r1
            m1 = cx * r2 + x * r3
            val r = (cy * m0 + y * m1).toInt()

            m0 = cx * g0 + x * g1
            m1 = cx * g2 + x * g3
            val g = (cy * m0 + y * m1).toInt()

            m0 = cx * b0 + x * b1
            m1 = cx * b2 + x * b3
            val b = (cy * m0 + y * m1).toInt()

            return a shl 24 or (r shl 16) or (g shl 8) or b
        }

        companion object {

            val ZERO = 0
            val CLAMP = 1
            val WRAP = 2

            val NEAREST_NEIGHBOUR = 0
            val BILINEAR = 1
        }


    } //end skew class

    companion object {

        private val useSunCodec = sunCodec
        private var JPEGCodec: Class<*>? = null
        private var JPEGEncodeParam: Class<*>? = null

        var InputFormats = getFormats(ImageIO.getReaderFormatNames())
        var OutputFormats = getFormats(ImageIO.getWriterFormatNames())


        //**************************************************************************
        //** getFormats
        //**************************************************************************
        /**  Used to trim the list of formats.  */

        private fun getFormats(inputFormats: Array<String>): Array<String> {
            var inputFormats = inputFormats

            //Build a unique list of file formats
            val formats = HashSet<String>()
            for (i in inputFormats.indices) {
                val format = inputFormats[i].uppercase()
                if (format.contains("JPEG") && format.contains("2000")) {
                    formats.add("JP2")
                    formats.add("J2C")
                    formats.add("J2K")
                    formats.add("JPX")
                } else if (format == "JPEG" || format == "JPG") {
                    formats.add("JPE")
                    formats.add("JFF")
                    formats.add(format)
                } else {
                    formats.add(format)
                }
            }

            //Sort and return the hashset as an array
            inputFormats = formats.toTypedArray()
            java.util.Collections.sort(java.util.Arrays.asList(*inputFormats))
            return inputFormats
        }


        //**************************************************************************
        //** getSunCodec
        //**************************************************************************
        /** Attempts to load classes from the com.sun.image.codec.jpeg package used
         * to compress jpeg images. These classes are marked as deprecated in Java
         * 1.7 and several distributions of Java no longer include these classes
         * (e.g.  "IcedTea" OpenJDK 7). Returns true of the classes are available.
         */
        private val sunCodec: Boolean
            get() {
                try {
                    JPEGCodec = Class.forName("com.sun.image.codec.jpeg.JPEGCodec")
                    JPEGEncodeParam = Class.forName("com.sun.image.codec.jpeg.JPEGEncodeParam")
                    return true
                } catch (e: Exception) {
                    return false
                }

            }


        //**************************************************************************
        //** getElementsByTagName (Copied from javaxt.xml.DOM)
        //**************************************************************************
        /** Returns an array of nodes that match a given tagName (node name). The
         * results will include all nodes that match, regardless of namespace. To
         * narrow the results to a specific namespace, simply include the namespace
         * prefix in the tag name (e.g. "t:Contact"). Returns an empty array if
         * no nodes are found.
         */
        private fun getElementsByTagName(tagName: String, node: Node): Array<Node> {
            val nodes = java.util.ArrayList<Node>()
            getElementsByTagName(tagName, node, nodes)
            return nodes.toTypedArray()
        }

        private fun getElementsByTagName(tagName: String, node: Node?, nodes: java.util.ArrayList<Node>) {
            if (node != null && node.nodeType.toInt() == 1) {

                var nodeName = node.nodeName.trim { it <= ' ' }
                if (nodeName.contains(":") && !tagName.contains(":")) {
                    nodeName = nodeName.substring(nodeName.indexOf(":") + 1)
                }

                if (nodeName.equals(tagName, ignoreCase = true)) {
                    nodes.add(node)
                }

                val childNodes = node.childNodes
                for (i in 0 until childNodes.length) {
                    getElementsByTagName(tagName, childNodes.item(i), nodes)
                }
            }
        }


        //**************************************************************************
        //** getAttributeValue (Copied from javaxt.xml.DOM)
        //**************************************************************************
        /**  Used to return the value of a given node attribute. The search is case
         * insensitive. If no match is found, returns an empty string.
         */
        fun getAttributeValue(attrCollection: NamedNodeMap?, attrName: String): String {

            if (attrCollection != null) {
                for (i in 0 until attrCollection.length) {
                    val node = attrCollection.item(i)
                    if (node.nodeName.equals(attrName, ignoreCase = true)) {
                        return node.nodeValue
                    }
                }
            }
            return ""
        }
    }

}//**************************************************************************
//** addText
//**************************************************************************
/**  Used to add text to the image at a given position.
 * @param x Lower left coordinate of the text
 * @param y Lower left coordinate of the text
 *///**************************************************************************
//** Resize (Overloaded Member)
//**************************************************************************
/**  Used to resize an image. Does NOT automatically retain the original
 * aspect ratio.
 *///**************************************************************************
//** trim
//**************************************************************************
/** Used to remove excess pixels around an image by cropping the image to its
 * "true" extents. Crop bounds are determined by finding the first non-null
 * or non-black pixel on each side of the image.
 */ //end image class