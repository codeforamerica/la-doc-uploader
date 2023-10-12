csvFile="${1:-"/Users/lkemperman/Desktop/digital-assister-all-designs-phase-1-multi-person-snap-only-leave-comments-here.csv"}"
outfileFilepath="${2:-messages_temp_3.properties}"
declare -A fieldMappings
declare -A pageTitles

preprocess_input () {
  columnValue=$1
  processedInput=${columnValue// /}
  echo $processedInput | sed -e 's/  */ /g' | tr A-Z a-z
}

while IFS="," read -r id frame group layer_name figma_text

# TODO: handle special characters
# TODO: make the field names generic for duplicates

do
  propertyName="$(preprocess_input $frame)"
  title=$( echo "${propertyName}.title" | tr -d '"')
  pageTitles+=( [$title]=${frame} )
  groupName="$(preprocess_input $group)"
  fieldName=$( echo $propertyName.$groupName | tr -d '"')
  if [ -z "$groupName" ] || [ -z "$figma_text" ]; then
     continue
  fi
  if [[ ${!fieldMappings[@]} =~ $fieldName ]]
  then
    ((fieldMappings["${fieldName}"]++))
  else
    fieldMappings+=( ["${fieldName}"]=0 )
  fi
  echo "${fieldName}${fieldMappings[$fieldName]}=${figma_text}" | tr -d "0" | tr -d '"' >> $outfileFilepath
done < <(sort -u -t, -k5,5 $csvFile) # this removes duplicate figma text
# adds page titles
for title in "${!pageTitles[@]}"; do
  echo "${title} = ${pageTitles["${title}"]}" >> $outfileFilepath
done
