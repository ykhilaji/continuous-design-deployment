import { useState, useEffect } from 'react'
import { FigmaNode } from "../model/figmaNode"
import { listFigmaNodes } from "./figmaFileTree"

export default function() {
  const [nodes, setNodes] = useState<FigmaNode[]>([])

  useEffect(() => {
    listFigmaNodes().then(res => setNodes(res))
  })

  return nodes
}
