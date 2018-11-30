import React from "react"
import { FigmaNode } from "../../model/figmaNode"
import { listFigmaNodes } from "../../services/figmaFileTree"
import { Treebeard } from "react-treebeard"

interface State {
  cursor: FigmaNode
  data: FigmaNode
}
interface Props {
  onClick?: (node: FigmaNode) => void
}

class FileTree extends React.Component<Props, State> {
  state: State = {
    data: {
      id: "",
      name: "loading",
      type: "",
      active: false,
    },
    cursor: {
      id: "",
      name: "loading",
      type: "",
      active: false,
    },
  }

  onToggle = (node: FigmaNode, toggled: boolean) => {
    if (this.state.cursor) {
      this.state.cursor.active = false
    }
    node.active = true
    if (node.children) {
      node.toggled = toggled
    }
    this.setState({ cursor: node })
    if (this.props.onClick) {
     this.props.onClick(node) 
    }
  }

  componentDidMount() {
    const fileKey = "RqrQf45ensLalBZWh4Bw6o6Q"
    listFigmaNodes(fileKey).then(res => {
      res.active = true
      this.setState({ data: res })
    })
  }

  public render() {
    return <Treebeard data={this.state.data} onToggle={this.onToggle} />
  }
}

export default FileTree
