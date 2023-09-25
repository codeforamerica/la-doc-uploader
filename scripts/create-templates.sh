#brew install yq
flowName="${1:-laDigitalAssister}"
flowIndex="${2:-1}"
templates=`ls -1 src/main/resources/templates/laDigitalAssister/*.html | cut -d'/' -f6-`
screens=( $(yq '[select(document_index == '"$flowIndex"') | .flow.*.nextScreens[0].name] | flatten' src/main/resources/flows-config.yaml) )
screenNames=( $( printf '%s\n' ${screens[@]} | egrep -v '^(---|-|null|\[*\])' ) )
for i in "${!screenNames[@]}"; do
  if grep -q "${screenNames[$i]}.html" <<< "${templates[*]}"; then
   echo "found ${screenNames[$i]} in templates"
  else
    echo "${screenNames[$i]} not in templates; creating one"
    cat scripts/scaffold.html >> "src/main/resources/templates/${flowName}/${screenNames[$i]}.html"
  fi
done
