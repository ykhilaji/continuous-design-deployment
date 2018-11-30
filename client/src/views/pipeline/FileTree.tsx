import React from "react"
import { Treebeard } from "react-treebeard"
import FigmaNodes from "../../services/FigmaNodes" 

export default function() {
  const nodes = FigmaNodes()
  return <Treebeard data={nodes}/> 
}
