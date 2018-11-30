import { FigmaNode } from "../model/figmaNode"

export function listFigmaNodes(): Promise<FigmaNode> {
  const figmaNodesMock: FigmaNode = {id: "1", name: "loading", type: "test"}  
  return new Promise<FigmaNode>(resolve => resolve(figmaNodesMock))
}
