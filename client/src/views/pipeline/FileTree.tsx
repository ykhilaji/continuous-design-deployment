import React from "react"
import { FigmaNode } from "../../model/figmaNode"
import { listFigmaNodes } from "../../services/figmaFileTree"

interface State {
  node: FigmaNode 
}

class FileTree extends React.Component<{}, State> {
  
  state: State = {
    node: {
      id: "",
      name:"",
      type:""
    }
  }

  public render(): JSX.Element {
    console.dir(this.state)
    return <div> </div>
  }

  componentDidMount() {
    const fileKey = "RqrQf45ensLalBZWh4Bw6o6Q"
    listFigmaNodes(fileKey).then(res => this.setState({ node: res }))
  }
}

export default FileTree
