import React, { useState } from "react"
// import { Switch, Route, withRouter } from "react-router-dom"
import SetupReact from "../../views/project/SetupReact"

import FormStepperBar from "./FormStepperBar"

const FormStepper = () => {
  const [pageIndex] = useState(1);

  return <>
      <FormStepperBar activeIndex={pageIndex}/>
      <SetupReact />
    </>
}

export default FormStepper
