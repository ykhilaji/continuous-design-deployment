import { FigmaNode } from "../model/figmaNode"

export function listFigmaNodes(): Promise<FigmaNode[]> {
  const figmaNodesMock: FigmaNode[] = [
    {
      id: "1",
      name: "Continuous Design Deployment",
      type: "",
    },
  ]
  return new Promise<FigmaNode[]>(resolve => resolve(figmaNodesMock))
}

