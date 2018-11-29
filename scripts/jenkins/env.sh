export JENKINS_URL="https://jenkins.znx.fr"

if [[ -z "$API_USER" ]]; then
  echo "ERROR: The environment variable API_USER is not set, please set it with:"
  echo "export API_USER='...'"
  echo ""
  echo "Note: you can get your jenkins API_USER from the page $JENKINS_URL/me/configure"
  exit 1
fi


if [[ -z "$API_TOKEN" ]]; then
  echo "ERROR: The environment variable API_TOKEN is not set, please set it with:"
  echo "export API_TOKEN='...'"
  echo ""
  echo "Note: you can get your jenkins API_TOKEN from the page $JENKINS_URL/me/configure"
  exit 1
fi
