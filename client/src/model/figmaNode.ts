export interface FigmaNode {
  id: string
  name: string
  type: string
  children?: FigmaNode[]
  active?: boolean
  toggled?: boolean
}

