import { useState } from 'react'
import { FigmaNode } from "../model/figmaNode"

export default function() {
  const [nodes] = useState<FigmaNode[]>([])
  return nodes
}
