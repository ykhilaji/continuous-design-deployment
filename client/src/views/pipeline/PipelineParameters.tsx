import React from "react"
import { Flex, Heading, Text, Box } from "rebass"
import Input from "../../components/Input"
import Switch, { SwitchType } from "../../components/Switch"

interface Props {
  assetName: string
}

const fileFormats: SwitchType[] = [
  {
    id: "svg",
    name: "svg",
    bg: "#FF6C58",
  },
  {
    id: "jpg",
    name: "jpg",
    bg: "#58B9FF",
  },
  {
    id: "png",
    name: "png",
    bg: "#58FFAF",
  },
]

export default function({ assetName }: Props) {
  return (
    <Flex width={1} flexDirection="column" p={3}>
      <Heading fontWeight="normal" fontSize={24}>
        Transformation de{" "}
        <Text fontWeight="bold" css={{ display: "inline-block" }}>
          {assetName}
        </Text>
      </Heading>
      <Box css={{ maxWidth: "500px" }}>
        <Box mt={5} />
        <Flex width={1}>
          <Box width={1}>
            <Text mb={2} fontSize={16} fontWeight="bold">
              Nom du fichier
            </Text>
            <Input placeholder={assetName} />
          </Box>
          <Switch labels={fileFormats} />
        </Flex>
        <Box mt={4}>
          <Text mb={2} fontSize={16} fontWeight="bold">
            Type de transformation
          </Text>
          <Flex alignItems="center">
            <Text mr={3}>Echelle</Text>
            <Input placeholder="1"/>
          </Flex>
        </Box>
        <Box mt={4}>
          <Text mb={2} fontSize={16} fontWeight="bold">
            Dossier de destination
          </Text>
          <Input prefix="image/" />
        </Box>
      </Box>
    </Flex>
  )
}
