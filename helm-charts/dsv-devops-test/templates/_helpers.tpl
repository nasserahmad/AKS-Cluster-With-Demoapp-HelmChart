{{/*
Docker registry ImagePullsecrets.
*/}}

{{- define "imagePullSecret" }}
{{- with .Values.imageCredentials }}
{{- printf "{\"auths\":{\"%s\":{\"reg_username\":\"%s\",\"reg_password\":\"%s\",\"email\":\"%s\",\"auth\":\"%s\"}}}" .registry .reg_username .reg_password .email (printf "%s:%s" .reg_username .reg_password | b64enc) | b64enc }}
{{- end }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "dsv-devops-test.labels" -}}
app: {{ include "dsv-devops-test.name" . }}
{{- end }}

{{/*
Expand the name of the chart.
*/}}
{{- define "dsv-devops-test.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "dsv-devops-test.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "dsv-devops-test.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}


{{/*
Create the name of the service account to use
*/}}
{{- define "dsv-devops-test.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "dsv-devops-test.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}
