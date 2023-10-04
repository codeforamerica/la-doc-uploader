csvFile="${1:-"/Users/lkemperman/Desktop/digital-assister-all-designs-phase-1-multi-person-snap-only-leave-comments-here.csv"}"
declare -A fieldMappings
while IFS="," read -r id frame group layer_name figma_text

do
  propertyName=${frame// /}
  propertyName=$(echo $propertyName | sed -e 's/  */ /g' | tr A-Z a-z)
  groupName=${group// /}
  groupName=$(echo $groupName | sed -e 's/  */ /g;s/([^)]*)//g' | tr A-Z a-z)
  fieldName=$( echo $propertyName.$groupName | tr -d '"')
  if [ -z "$propertyName" ] || [ -z "$groupName" ] || [ -z "$figma_text" ]; then
     continue
  fi
  if [[ ${!fieldMappings[@]} =~ $fieldName ]]
  then
    ((fieldMappings["${fieldName}"]++))
  else
    fieldMappings+=( ["${fieldName}"]=0 )
  fi
  echo "${fieldName}${fieldMappings[$fieldName]}=${figma_text}" | tr -d "0" | tr -d '"' >> messages_temp.properties
done < <(tail -n +2 $csvFile)