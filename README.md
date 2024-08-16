mbt build -t SampleSAPBTPApplication/generatedTAR --mtar SampleApplication.tar

cf login -a https://<<CF CPI URL>>.hana.ondemand.com

cf deploy SampleSAPBTPApplication/generatedTAR/SampleApplication.tar
