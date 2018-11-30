import React, { useState } from "react"
import { Flex, Heading, Text, Box , Button} from "rebass"
import Input from "../../components/Input"
import Switch, { SwitchType } from "../../components/Switch"
import { pushPR } from "../../services/figmaPushApi";

const fileKey = 'RqrQf45ensLalBZWh4Bw6o6Q'
interface Props {
  assetName: string
  ids: Id[]
}

interface Id {
  id: string
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

export default function({ assetName, ids }: Props) {
  const [form, setForm] = useState({
    nom: "",
    type: "",
    dossier: "",
    extension: ""
  })
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
            <Input placeholder={assetName} onChange={(e) => setForm(Object.assign(form, { nom: e.target.value}))} />
          </Box>
          <Switch labels={fileFormats} onChange={(value) => setForm(Object.assign(form, { extension: value })) }/>
        </Flex>
        <Box mt={4}>
          <Text mb={2} fontSize={16} fontWeight="bold">
            Type de transformation
          </Text>
          <Flex alignItems="center">
            <Text mr={3}>Echelle</Text>
            <Input placeholder="1" onChange={(e) => setForm(Object.assign(form, { type: e.target.value}))}/>
          </Flex>
        </Box>
        <Box mt={4}>
          <Text mb={2} fontSize={16} fontWeight="bold">
            Dossier de destination
          </Text>
          <Input prefix="image/" onChange={(e) => setForm(Object.assign(form, { dossier: e.target.value}))}/>
        </Box>
        <Box css={{ display: 'flex', alignItems: 'flex-end'}}>
          <Button 
            onClick={() => pushPR(fileKey, ids)}
            css={{marginTop: '30px'}}
          >Valider</Button>
        </Box>
      </Box>
    </Flex>
  )
}
