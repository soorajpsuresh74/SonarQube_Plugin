api_value = env.secret
aws_key_akia = "AKIAEXAMPLE1234567890" #Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: 'AKIAEXAMPLE1234567890'}}
aws_key_asia = "ASIAEXAMPLE1234567890" #Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: 'ASIAEXAMPLE1234567890'}}

def example_function():
    aws_key_in_function = "AKIAANOTHEREXAMPLE123456" #Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: 'AKIAANOTHEREXAMPLE123456'}}
    return aws_key_in_function

aws_key_asia = "ASIAEXAMPLE1234567890ywdyvwyvdgwvdgvwdgvgwqvdgqvdqvduqvdulvdulvwduvwulv" #Noncompliant {{Likely identified as sensitive key exposure. Please cross-verify. Found: 'ASIAEXAMPLE1234567890ywdyvwyvdgwvdgvwdgvgwqvdgqvdqvduqvdulvdulvwduvwulv'}}
