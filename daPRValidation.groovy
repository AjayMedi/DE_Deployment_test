def call(def gitLog, boolean isPRRequired,boolean isCRRequired,boolean isTestCaseRequired, String env) {
    //Parameters used/set in daPRValidation
    def crNo = 'none'
    def release = 'none'
    def deploymentOrder = 'none'
    def isLast = 'true'
    def testCases = 'none'
    def functionalArea = 'none'
    def details = 'none'

    //This function validates the deployment parameters provided in the PR title format.
    //PR title guidelines: https://confluence.ameritas.com/display/DA/Bitbucket+-+Pull+Request+Best+Practices

    def prTitleFormat = "<a href='https://confluence.ameritas.com/display/DA/Bitbucket+-+Pull+Request+Best+Practices'> PR Title Guidelines</a>"

    def validFunctionalAreas = ["dfo","aip","alm","aiex","odm",
                                "rphub","events","product",
                                "finance","customer","contract",
                                "valuation","operations","individual",
                                "investment","producer", "compensation", "datascience", "operations", "dataops"]

    if (gitLog.contains('Pull')) {
        def prTitle = gitLog

        def prNo="prNo-";
        if(prTitle.contains('#') && prTitle.contains(':')){
            tempPrNo = prTitle.substring(prTitle.indexOf("#") + 1);
            prNo = prNo + tempPrNo.substring(0, tempPrNo.indexOf(":"));
        }
        println("prNo: " + prNo)

        try {
            def tempPrTitle = prTitle.minus(prTitle.split(':')[0])
            tempPrTitle = tempPrTitle.substring(1)
            println("tempPrTitle: " + tempPrTitle)

            if (tempPrTitle.split(';')[0].split('=')[0].trim().equalsIgnoreCase('cr_no') &&
                    tempPrTitle.split(';')[1].split('=')[0].trim().equalsIgnoreCase('release') &&
                    tempPrTitle.split(';')[2].split('=')[0].trim().equalsIgnoreCase('deployment_order') &&
                    tempPrTitle.split(';')[3].split('=')[0].trim().equalsIgnoreCase('is_last_deployment') &&
                    tempPrTitle.split(';')[4].split('=')[0].trim().equalsIgnoreCase('test_cases') &&
                    tempPrTitle.split(';')[5].split('=')[0].trim().equalsIgnoreCase('functional_area') &&
                    tempPrTitle.split(';')[6].split('=')[0].trim().equalsIgnoreCase('details')
            ) {

                crNo = tempPrTitle.split(';')[0].split('=')[1].trim()
                release = tempPrTitle.split(';')[1].split('=')[1].trim()
                deploymentOrder = tempPrTitle.split(';')[2].split('=')[1].trim()
                isLast = tempPrTitle.split(';')[3].split('=')[1].trim()
                testCases = tempPrTitle.split(';')[4].split('=')[1].trim()
                functionalArea = tempPrTitle.split(';')[5].split('=')[1].trim()
                details = tempPrTitle.split(';')[6].split('=')[1].trim()

                println("cr_no: " + crNo)
                println("release: " + release)
                println("deployment_order:" + deploymentOrder)
                println("is_last_deployment: " + isLast)
                println("test_cases: " + testCases)
                println("functional_area: " + functionalArea)
                println("details: " + details)

            } else {
                throw new Exception("Incorrect PR title format!  <BR> Please refer the " +
                        prTitleFormat )
            }

        }
        catch (Exception e) {
            throw new Exception("Incorrect PR title format!  <BR> Please refer the " +
                    prTitleFormat )
        }

        if (!validFunctionalAreas.contains(functionalArea.toLowerCase())) {
            throw new Exception("Invalid FunctionalArea in PR title format! " +
                    " <BR> Following are the list of valid Function Areas: "+validFunctionalAreas +
                    "Please refer the PR title guidelines:" +prTitleFormat)
        }

        if (isCRRequired) {
            if (crNo == "" || crNo.toUpperCase() == "NONE") {
                throw new Exception("Valid CR# is required for "+env+" environment! <BR> Please refer the " +
                        prTitleFormat )
            }
        }

        if(isTestCaseRequired){
            if (testCases == "" || testCases.toUpperCase() == "NONE") {
                throw new Exception("Valid testCases are required for "+env+" environment! <BR> Please refer the " +
                        prTitleFormat )
            }
        }

    } else {
        if (isPRRequired) {
            throw new Exception("PR is required for "+env+" environment! <BR> Please refer the PR " +
                    prTitleFormat )
        }
    }

    return [crNo,release,deploymentOrder,isLast,testCases,functionalArea,details]
}
