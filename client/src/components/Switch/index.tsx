import React, { useState } from "react"
import { Box, Button } from "rebass"

export type SwitchType = {
  id: string
  name: string
  bg: string
}

interface Props {
  labels: SwitchType[]
  onChange?: (value: string) => void
}

const Switch = ({ labels, onChange }: Props) => {
  const [activeLabel, setActiveLabel] = useState(labels[0])
  if (labels.length === 0) {
    return null
  }

  const handleChange = (label: SwitchType) => {
    setActiveLabel(label)
    onChange && onChange(label.id)
  } 

  return <Box 
    css={{ display: 'flex'}}
  >
      {labels.map(label => {
        const handleClick = () => handleChange(label)

        return <SwitchButton
          onClick={handleClick}
          active={label.id === activeLabel.id}
          bg={label.bg}
          name={label.name}
          key={label.id} />
        })} 

    </Box>
}

export default Switch

function SwitchButton({ name, bg, active = false, key, onClick }: { name: string, bg: string, active: boolean, key: string, onClick: () => void }) {

  return <Button css={{margin: '0 10px'}} key={key} bg={active ? "#1e76fc" : bg} onClick={onClick}>{name}</Button>
}
