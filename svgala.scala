// create an svg display object
//package svgala

import scala.collection.mutable.ListBuffer
import java.io._

class SVGala(val width: Int, val height: Int) {

  // list of objects to display
  val displayList = ListBuffer[String]()

  // utilities

  // get the attribute name from an attribute assignment string
  def getAttrName(attr: String): String = attr.split('=')(0)

  // move origin to lower left by adjusting y coordinate
  def flipY(y: Int, oHeight: Int): Int = height - (y + oHeight)

  // uniform DRY handling of presentation attributes
  // takes the element specific part of the definition and combines it with
  // any presentation attributes added as an array of Strings.
  // Completes the svg tag and returns the entire string to add to the document.
  // apply defauls and filter out override of defaults.
  def addPresentationAttr(element: String, attr: Seq[String], defaultAttr: Array[String]): String = {
    val attrNames = attr.map((a: String) => getAttrName(a)) // just attr name
    val filteredDefaultAttr = defaultAttr.filter((da: String) => !attrNames.contains(getAttrName(da)))
    val finalAttr = attr ++ filteredDefaultAttr
    element + finalAttr.reduce((a: String, b: String) => a + " " + b) + "/>"
  }

  // methods which create object tags as strings

  // Create an object tag for a rectangle
  def makeRect(x: Int, y: Int, width: Int, height: Int, attr: Seq[String]): String = {
    val svg1 = s"""<rect x="$x" y="${flipY(y, height)}" width="$width" height="$height" """
    val defaultAttr = Array("""fill="0X0000FF"""", """stroke-width="1"""", """stroke="0x000000"""")
    addPresentationAttr(svg1, attr, defaultAttr)
  }

  // methods which add objects to the display

  // add a rectangle
  def addRect(x: Int, y: Int, width: Int, height: Int, attr: String*) {
    displayList += makeRect(x, y, width, height, attr)
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
