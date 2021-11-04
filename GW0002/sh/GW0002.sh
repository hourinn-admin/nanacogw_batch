#!/bin/sh

PROPERTY_LIST=$1

if [ $# -eq 1 ] ; then
	if [! -r "$PROPERTY_LIST" ] ; then
		echo "設定ファイルの読込に失敗しました。(ファイル名:$PROPERTY_LIST)"
		exit 1
	fi
else 
	echo "引数の指定が不正です。"
	exit 1
fi

. $PROPERTY_LIST

ES_PROG_ID=GW0002
ES_LOG_FILE_NAME=GW0002
LOG_FILE_NAME=${ES_PROG_ID}_$DATE_YYYYMMDD.log
LOGFILE=$ES_LOG_PATH/$LOG_FILE_NAME

DISP_LOG_ID=$ES_PROG_ID

echo "[$DATE_DISP_LOG] Start Batch    		[$DISP_LOG_ID]実行開始" >>${LOGFILE} 2>&1

CSV_FILE_PATH=`ls -tr $BATCH_CSV_FILE_APPLY_DIR/$BASE_CSV_FILE_APPLY_NAME*.csv 2> /dev/null | head -n 1`

if [ "$CSV_FILE_PATH" = "" ] ; then
	echo "[$DATE_DISP_LOG] Info       		[$DISP_LOG_ID]取込対象データが存在しません。{$BASE_CSV_FILE_APPLY_NAME}" >>${LOGFILE} 2>&1
	echo "[$DATE_DISP_LOG] Finish Batch 	[$DISP_LOG_ID]実行終了" >>${LOGFILE} 2>&1
	exit 0
fi


export ES_FILE_NAME=`basename ${CSV_FILE_PATH}`
export ES_FILE_NAME=`${BATCH_CSV_FILE_APPLY_DIR}`
export CSV_FILE_PATH


exp_list=""
while read line;
do
	case $line in
	\#*)
		continue;
		;;
	'')
		continue;
		;;
	*=*)
		prop=`echo ${line} | cut -d = -f 1`
		;;
	esac
	
	exp_list="${exp_list} ${prop}"

done < $PROPERTY_LIST

export ${exp_list}


export NANACO_SET_REQUEST_DATA_DIR  = /module/app/sub/ONG/GW0002/
export LIB_DIR = ${NANACO_SET_REQUEST_DATA_DIR}lib/
export CONF_DIR = ${NANACO_SET_REQUEST_DATA_DIR}conf/
export SH_DIR = ${NANACO_SET_REQUEST_DATA_DIR}sh/

LIBARIES=${LIB_DIR}ojdbc8-19.3.0.0.jar:${LIB_DIR}log4j-1.2.17.jar:${LIB_DIR}GW0002.jar

CP=${LIBARIES}:${CONF_DIR}
java -cp ${CP} -Djava.security.egd=file:/dev/../dev/urandom nanacosetrequestdata.NanacoSetRequestData $1

if [ $status -ne 0 ] ; then
	echo "ポイント付与依頼ファイル取込処理に失敗しました。"　"ファイル名:$ES_FILE_NAME"
	echo "[$DATE_DISP_LOG] Error 			[$DISP_LOG_ID]ポイント付与依頼ファイル取込に失敗しました。" >>${LOGFILE} 2>&1
	echo "[$DATE_DISP_LOG] Error 			[$DISP_LOG_ID]ファイル名:$ES_FILE_NAME" >>${LOGFILE} 2>&1
	echo "[$DATE_DISP_LOG] Finish Batch 	[$DISP_LOG_ID]実行終了" >>${LOGFILE} 2>&1
	echo $status
	exit $status
fi

CSV_FILE_FULL_PATH=`ls -t "$BATCH_FORMAT_ERROR_LOG_DIR/$BASE_FORMAT_ERROR_LOG_FILE_NAME"*.txt 2> /dev/null | head -n 1`
CSV_FILE_SIZE=`ls -l ${CSV_FILE_FULL_PATH} | tr -s ' ' | cut -d ' ' -f5 -`
if [ "$CSV_FILE_SIZE" -le 0 ] ; then
	rm -f $CSV_FILE_FULL_PATH
else
	mv $CSV_FILE_FULL_PATH $BATCH_FORMAT_ERROR_LOG_BK_DIR 2> /dev/null
fi

mv $ES_FILE_PATH/$ES_FILE_NAME  $BATCH_CSV_FILE_APPLY_BK_DIR/$BATCH_CSV_FILE_APPLY_BK_NAME 2> /dev/null
status=$?
if [ $status -eq 0 ] ; then
	echo "ポイント付与依頼を成功に取込ました。"　"申込ファイル: ${CSV_FILE_PATH}をBATCHサーバのバックアップフォルダに格納しました。"
    echo "[$DATE_DISP_LOG]					[$DISP_LOG_ID]ポイント付与依頼の取込が正常終了しました。" >>${LOGFILE} 2>&1
    echo "[$DATE_DISP_LOG]					[$DISP_LOG_ID]申込ファイル:{$CSV_FILE_PATH}をBATCHサーバのバックアップフォルダに格納しました。" >>${LOGFILE} 2>&1
else
	echo "申込ファイル:{$DATA_FILE_PATH}をBATCHサーバのバックアップに格納できませんでした。"
	echo "[$DATE_DISP_LOG] Error 			[$DISP_LOG_ID]申込ファイル:{$CSV_FILE_PATH}をBATCHサーバのバックアップフォルダに格納できませんでした。" >>${LOGFILE} 2>&1	     
fi

echo "[$DATE_DISP_LOG] Finish Batch	    [$DISP_LOG_ID]実行終了" >>${LOGFILE} 2>&1
echo $status
echo $status








