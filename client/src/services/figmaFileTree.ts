import { FigmaNode } from "../model/figmaNode"

export function listFigmaNodes(fileKey: String): Promise<FigmaNode> {
  return fetch(`/api/assets/documentTree/${fileKey}`)
  .then(response => {
    if (response.ok) {
      return response.json()
    } else {
      throw {
        name: 'FetchError',
        status: response.status
      }
    }
  }).then(json => json as FigmaNode)
}
