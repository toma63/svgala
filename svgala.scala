// create an svg display object
package svgala

import scala.collection.mutable.ListBuffer
import java.io._

class SVGala(val width: Int, val height: Int) {

  // list of objects to display
  val displayList = ListBuffer[String]()

  // utilities

  // move origin to lower left by adjusting y coordinate
  def flipY(y: Int, oHeight: Int): Int = height - (y + oHeight)

  // methods which add objects to the display

  // add a rectangle
  def addRect(x: Int, y: Int, width: Int, height: Int,
    fill: String = "0X0000FF", strokeWidth: String = "1", stroke: String = "0x000000") {

    val svg1 = s"""<rect x="$x" y="${flipY(y, height)}" width="$width" height="$height" """
    val svg2 = svg1 + s"""fill="$fill" stroke-width="$strokeWidth" stroke="$stroke"/>"""
    displayList += svg2
  }

  // output
  def writeSVG(fileName: String) {
    val bw = new BufferedWriter(new FileWriter(fileName))

    // add the header
    val header = s"""<?xml version="1.0"?>
<!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN"
  "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">

<svg width="$width" height="$height" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
"""
    bw.write(header)
    bw.newLine

    // add the display objects
    displayList.foreach(obj => bw.write(obj + "\n"))

    // close the svg doc
    bw.newLine
    bw.write("</svg>\n")

    bw.close()
  }

}
