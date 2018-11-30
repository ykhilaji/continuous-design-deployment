import React from "react"
import { Flex, Text, Image } from "rebass"
import SelectAll from "./selectAll.svg"

export default function() {
  return (
    <Flex width={1} flexDirection="column" alignItems="center" justifyContent="center" css={{ height: "100%" }}>
      <Image mb={3} src={SelectAll} />
      <Text
        textAlign="center"
        lineHeight={2}
        color="#C4C4C4"
        fontWeight="bold"
        fontSize={18}
        css={{ maxWidth: "600px" }}
      >
        Sélectionnez un asset dans la colonne de gauche pour lui appliquer une
        transformation.
      </Text>
    </Flex>
  )
}
