import React from "react"
import { Text, Flex } from "rebass"

interface Props {
  num: number
  actionWord: string
  subText: string
  active?: boolean
  showSeparator?: boolean
}

export default function({ num, active = false, actionWord, subText, showSeparator = true }: Props) {
  return (
    <Flex
      alignItems="center"
      css={{
        textTransform: "uppercase",
        fontWeight: "bold",
        color: active ? "black" : "#949494",
      }}
    >
      <Text mr={15} fontSize={20}>
        {num}
      </Text>
      <Text fontSize={14} css={{ display: "flex", textDecoration: active ? "underline" : "none" }}>
        {actionWord}&nbsp;
        <span style={{"fontWeight": "normal"}}>
          {subText}
        </span>
      </Text>
      {showSeparator && <Text fontWeight="normal" color="#949494" mx={4}>|</Text>}
    </Flex>
  )
}
